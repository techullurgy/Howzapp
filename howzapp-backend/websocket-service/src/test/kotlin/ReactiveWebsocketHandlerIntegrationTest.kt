import com.techullurgy.howzapp.common.core.pubsub.IPubSubManager
import com.techullurgy.howzapp.common.core.pubsub.PubSubConstants
import com.techullurgy.howzapp.websocket.HowzappWebsocketServiceApplication
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import java.net.URI
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [HowzappWebsocketServiceApplication::class]
)
@ActiveProfiles("internal-dev")
class ReactiveWebsocketHandlerIntegrationTest {

    @LocalServerPort
    private var port: Int = 0

    @MockitoBean
    private lateinit var pubSubManager: IPubSubManager

    private val client = ReactorNettyWebSocketClient()

    @Test
    fun `websocket client receives outbound messages from custom pubSubManager`(): Unit = runBlocking {
        val userId = "user_789"
        val userChannel = "${PubSubConstants.USER_CHANNEL_PREFIX}:$userId"
        val expectedMessage = """{"type":"CHAT","content":"Hello world!"}"""

        // Shared Flow simulating PubSub emits
        val pubSubChannel = MutableSharedFlow<String>(replay = 1)
        `when`(pubSubManager.listenTo(userChannel)).thenReturn(pubSubChannel)

        val wsUri = URI.create("ws://localhost:$port/ws?userId=$userId")

        val clientJob = async {
            client.execute(wsUri) { session ->
                mono {
                    val inboundFlow = session.receive()
                        .map { it.payloadAsText }
                        .asFlow()

                    // Wait 200ms for handler setup
                    delay(200.milliseconds)

                    // Emit payload into custom IPubSubManager stream
                    pubSubChannel.emit(expectedMessage)

                    // Verify frame received on WebSocket client
                    val receivedMessage = withTimeout(5.seconds) {
                        inboundFlow.first()
                    }

                    assertEquals(expectedMessage, receivedMessage)
                    null
                }
            }.then().toFuture().get()
        }

        clientJob.await()
    }
}