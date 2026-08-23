package com.techullurgy.howzapp.conversation.service

import com.techullurgy.howzapp.common.dto.CreateConvResponse
import com.techullurgy.howzapp.conversation.db.entities.ConversationParticipantEntity
import com.techullurgy.howzapp.conversation.db.repositories.ConversationRepository
import com.techullurgy.howzapp.conversation.db.repositories.ParticipantRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toSet
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DirectConversationService(
    private val conversationRepository: ConversationRepository,
    private val participantRepository: ParticipantRepository
) {

    /**
     * Finds an existing 1-on-1 direct conversation between userA and userB by intersecting
     * their `is_direct = true` user_conversations entries.
     */
    suspend fun findExistingDirectConversation(userA: String, userB: String): String? = coroutineScope {
        val userAConvsDeferred = async { conversationRepository.findDirectConversationIdsForUser(userA).toSet() }
        val userBConvsDeferred = async { conversationRepository.findDirectConversationIdsForUser(userB).toSet() }

        val convsA = userAConvsDeferred.await()
        val convsB = userBConvsDeferred.await()

        convsA.intersect(convsB).firstOrNull()
    }

    /**
     * Idempotently gets or creates a 1-on-1 direct conversation.
     */
    suspend fun getOrCreateDirectConversation(userA: String, userB: String): CreateConvResponse {
        val existingConvId = findExistingDirectConversation(userA, userB)
        if (existingConvId != null) {
            return CreateConvResponse(conversationId = existingConvId, isNew = false)
        }

        val newConvId = "conv_${UUID.randomUUID().toString().replace("-", "").take(16)}"

        participantRepository.addParticipant(ConversationParticipantEntity(newConvId, userA))
        participantRepository.addParticipant(ConversationParticipantEntity(newConvId, userB))

        conversationRepository.updateUserConvMeta(userA, newConvId, seqId = 0, isDirect = true)
        conversationRepository.updateUserConvMeta(userB, newConvId, seqId = 0, isDirect = true)

        return CreateConvResponse(conversationId = newConvId, isNew = true)
    }
}