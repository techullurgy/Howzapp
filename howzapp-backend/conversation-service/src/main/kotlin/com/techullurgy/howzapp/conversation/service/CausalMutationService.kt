package com.techullurgy.howzapp.conversation.service

import com.techullurgy.howzapp.common.dto.InternalAppendRequest
import com.techullurgy.howzapp.common.dto.InternalAppendResponse
import com.techullurgy.howzapp.conversation.db.entities.ConversationParticipantEntity
import com.techullurgy.howzapp.conversation.db.entities.MessageEntity
import com.techullurgy.howzapp.conversation.db.repositories.ConversationRepository
import com.techullurgy.howzapp.conversation.db.repositories.ParticipantRepository
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import java.time.Instant

data class MutationExecutionResult(
    val serverSeqId: Long,
    val remappedConvId: String? = null
)

@Service
class CausalMutationService(
    private val repository: ConversationRepository,
    private val participantRepository: ParticipantRepository,
    private val directConvService: DirectConversationService,
    private val objectMapper: ObjectMapper
) {

    suspend fun processOrderedMutations(
        senderId: String,
        mutations: List<InternalAppendRequest>
    ): List<InternalAppendResponse> {
        val results = mutableListOf<InternalAppendResponse>()
        val groupedByConv = mutations.groupBy { it.conversationId }

        for ((convId, convMutations) in groupedByConv) {
            val sortedMutations = convMutations.sortedBy { it.clientSeqId }
            var isChainBroken = false
            var activeRemappedConvId: String? = null // Track remapped ID across the batch if collision occurs

            for (mutation in sortedMutations) {
                if (isChainBroken) {
                    results.add(
                        InternalAppendResponse(
                            mutationId = mutation.clientMutationId,
                            clientSeqId = mutation.clientSeqId,
                            status = "BLOCKED_BY_PREVIOUS_FAILURE",
                            errorMessage = "Execution halted due to failure of an earlier mutation in the sequence queue."
                        )
                    )
                    continue
                }

                try {
                    // Pass activeRemappedConvId if an earlier item in this loop was already remapped
                    val effectiveConvId = activeRemappedConvId ?: convId
                    val executionResult = executeSingleMutation(senderId, effectiveConvId, mutation)

                    if (executionResult.remappedConvId != null) {
                        activeRemappedConvId = executionResult.remappedConvId
                    }

                    results.add(
                        InternalAppendResponse(
                            mutationId = mutation.clientMutationId,
                            clientSeqId = mutation.clientSeqId,
                            status = "SUCCESS",
                            serverSeqId = executionResult.serverSeqId,
                            remappedConvId = executionResult.remappedConvId
                        )
                    )
                } catch (ex: Exception) {
                    isChainBroken = true
                    results.add(
                        InternalAppendResponse(
                            mutationId = mutation.clientMutationId,
                            clientSeqId = mutation.clientSeqId,
                            status = "FAILED",
                            errorMessage = ex.message ?: "Execution error"
                        )
                    )
                }
            }
        }

        return results
    }

    private suspend fun executeSingleMutation(
        senderId: String,
        convId: String,
        mutation: InternalAppendRequest
    ): MutationExecutionResult {
        return when (mutation.mutationType) {
            "SEND_MESSAGE" -> {
                val node = objectMapper.readTree(mutation.payload)
                val contentType = node.get("contentType")?.asText() ?: "TEXT"
                val body = node.get("body")?.asText()
                val recipientId = node.get("recipientId")?.asText()

                var remappedId: String? = null
                var targetConvId = convId

                // Check for concurrent offline collision if recipientId is present
                if (recipientId != null) {
                    val existingConvId = directConvService.findExistingDirectConversation(senderId, recipientId)

                    if (existingConvId != null && existingConvId != convId) {
                        // COLLISION DETECTED: The other user synced first and created existingConvId. Remap to it!
                        targetConvId = existingConvId
                        remappedId = existingConvId
                    } else if (existingConvId == null) {
                        // NO COLLISION & FIRST SYNC: Register participants for this new conversation ID
                        participantRepository.addParticipant(ConversationParticipantEntity(convId, senderId))
                        participantRepository.addParticipant(ConversationParticipantEntity(convId, recipientId))
                    }
                }

                // Increment sequence counter on the target (remapped or original) conversation
                val seqId = repository.incrementAndGetSeqId(targetConvId)

                val entity = MessageEntity(
                    conversationId = targetConvId,
                    seqId = seqId,
                    messageId = mutation.clientMutationId,
                    senderId = senderId,
                    contentType = contentType,
                    body = body,
                    status = "SENT",
                    createdAt = Instant.now()
                )

                repository.saveMessage(entity)

                // Update inbox records (user_conversations) for all participants
                val participants = participantRepository.findParticipants(targetConvId).toList()
                val isDirect = participants.size == 2

                for (participant in participants) {
                    repository.updateUserConvMeta(
                        userId = participant.userId,
                        conversationId = targetConvId,
                        seqId = seqId,
                        isDirect = isDirect
                    )
                }

                MutationExecutionResult(serverSeqId = seqId, remappedConvId = remappedId)
            }

            "READ_RECEIPT" -> {
                val node = objectMapper.readTree(mutation.payload)
                val targetSeqId = node.get("seqId").asLong()
                repository.updateMessageStatus(convId, targetSeqId, "READ")
                MutationExecutionResult(serverSeqId = targetSeqId)
            }

            "DELIVERY_RECEIPT" -> {
                val node = objectMapper.readTree(mutation.payload)
                val targetSeqId = node.get("seqId").asLong()
                repository.updateMessageStatus(convId, targetSeqId, "DELIVERED")
                MutationExecutionResult(serverSeqId = targetSeqId)
            }

            else -> throw IllegalArgumentException("Unsupported mutation type: ${mutation.mutationType}")
        }
    }
}