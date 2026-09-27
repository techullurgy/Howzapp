package com.techullurgy.howzapp.root.database.test

import com.techullurgy.howzapp.core.database.Database
import com.techullurgy.howzapp.feature.chats.db.entities.ConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.GroupConversationEntity
import com.techullurgy.howzapp.feature.chats.db.entities.GroupConversationParticipantsCrossRef
import com.techullurgy.howzapp.feature.chats.db.models.GroupParticipantTypeStored
import com.techullurgy.howzapp.feature.users.db.entities.UserEntity
import com.techullurgy.howzapp.feature.users.db.models.UserExistTypeStored
import com.techullurgy.howzapp.root.database.HowzappDatabaseImpl
import com.techullurgy.howzapp.root.database.HowzappRoomDatabase
import com.techullurgy.howzapp.root.database.test.roomShared.howzappDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class RoboTest {

    private lateinit var database: HowzappRoomDatabase

    private lateinit var trDB: Database

    @BeforeTest
    fun setup() {
        database = howzappDatabase()
        trDB = HowzappDatabaseImpl(database)
    }

    @AfterTest
    fun tearDown() {
        database.close()
    }

    @Test
    fun test() = runTest {
        val exc = Random.nextInt(10, 20) % 2 == 0
        try {
            trDB.withWriteTransaction {
                database.userDao.upsertUser(
                    UserEntity(
                        userId = "123",
                        userExistType = UserExistTypeStored.OTHER,
                        contact = "892",
                        displayName = "Irsath",
                        avatarUrl = null,
                        lastSeenTime = null
                    )
                )
                database.userDao.upsertUser(
                    UserEntity(
                        userId = "456",
                        userExistType = UserExistTypeStored.OTHER,
                        contact = "382",
                        displayName = "Grut",
                        avatarUrl = "Grut Avatar",
                        lastSeenTime = null
                    )
                )

                database.conversationDao.upsertConversation(ConversationEntity("c_11"))
                database.conversationDao.upsertGroupConversation(
                    GroupConversationEntity(
                        conversationId = "c_11",
                        title = "People, ILove",
                        avatarUrl = "Timber avatar",
                        createdAt = Clock.System.now().minus(20.minutes)
                    )
                )

                database.conversationDao.upsertGroupParticipants(
                    GroupConversationParticipantsCrossRef(
                        conversationId = "c_11",
                        userId = "123",
                        joinedAt = Clock.System.now().minus(10.minutes),
                        type = GroupParticipantTypeStored.ADMIN
                    ),
                    GroupConversationParticipantsCrossRef(
                        conversationId = "c_11",
                        userId = "456",
                        joinedAt = Clock.System.now().minus(2.minutes),
                        type = GroupParticipantTypeStored.MEMBER
                    )
                )

                println(database.conversationDao.observeConversation("c_11").first())

                if(exc) throw Exception()
            }
        } catch(_: Exception) {} finally {
            val res = database.conversationDao.observeConversation("c_11").first()
            if (exc) {
                assertNull(res)
            } else {
                assertNotNull(res)
            }
        }
    }
}