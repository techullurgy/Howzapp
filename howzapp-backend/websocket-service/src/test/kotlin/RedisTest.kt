//@SpringBootTest(classes = [HowzappWebsocketServiceApplication::class])
//@Testcontainers
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) // Closes Netty sockets before container stops
//class RedisTest {
//
//    @Autowired
//    private lateinit var redisTemplate: ReactiveStringRedisTemplate
//
//    @Autowired
//    private lateinit var container: ReactiveRedisMessageListenerContainer
//
//    @Test
//    fun redisConnectionTest(): Unit = runBlocking {
//        val channelName = "test:channel:user_123"
//        val expectedMessage = """{"type":"TEXT","text":"Hello world over Redis PubSub"}"""
//
//        // 1. Launch a background coroutine to start collecting immediately
//        val deferredMessage = async {
//            withTimeout(5000.milliseconds) {
//                container.receive(ChannelTopic(channelName))
//                    .map { it.message }
//                    .asFlow()
//                    .first() // Subscribes NOW in the background
//            }
//        }
//
//        // 2. Wait 300ms to guarantee the SUBSCRIBE TCP packet reaches Redis
//        delay(300.milliseconds)
//
//        // 3. Publish the message to Redis
//        redisTemplate.convertAndSend(channelName, expectedMessage).awaitSingle()
//
//        // 4. Await the result from the background listener
//        val receivedMessage = deferredMessage.await()
//
//        assertEquals(expectedMessage, receivedMessage)
//    }
//
//
//    companion object {
//        @Container
//        @JvmStatic
//        val redisContainer: GenericContainer<*>? = GenericContainer(DockerImageName.parse("redis:7.4.10-alpine"))
//            .withExposedPorts(6379)
//
//        @DynamicPropertySource
//        @JvmStatic
//        fun registerRedisProperties(registry: DynamicPropertyRegistry) {
//            registry.add("spring.data.redis.host") { redisContainer!!.host }
//            registry.add("spring.data.redis.port") { redisContainer!!.getMappedPort(6379) }
//        }
//    }
//}