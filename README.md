# howzapp

## 1. Description
**howzapp** is an enterprise distributed, real-time instant messaging microservices platform (WhatsApp clone) designed for high-concurrency messaging, media sharing, status updates, and live presence tracking. The system pairs a reactive Spring Boot backend microservice mesh powered by Redis PubSub event broadcasting and S3 media presigning with a cross-platform Kotlin Multiplatform (KMP) client application.

---

## 2. Technical Special Aspects
- **Distributed Microservices Mesh**:
  - **`websocket-service`**: Handles persistent, low-latency client WebSocket connections via Spring WebFlux `ReactiveWebsocketHandler` and connection session registries.
  - **`user-service`**: Manages user profiles, privacy configurations, and real-time online/offline presence with invalidation listeners (`UserPresenceEventListener`, `UserPresenceCachedRepository`).
  - **`conversation-service`**: Features a Causal Mutation Engine (`CausalMutationService`) to ensure strict message ordering, participant state synchronization, and delivery confirmations (sent, delivered, read).
  - **`sync-service`**: Orchestrates catch-up message history synchronization for clients reconnecting after being offline (`SyncController`).
  - **`media-service`**: Secure S3 presigned URL generator (`S3PresignerService`) for direct-to-cloud encrypted media uploads.
  - **`status-service`**: Manages ephemeral status/story media posts with expiration TTLs.
- **Event-Driven PubSub Architecture**: Inter-service real-time event broadcasting and multi-node message fanout powered by Redis PubSub (`RedisPubSubManager`).
- **KMP Cross-Platform Client Architecture**: Multiplatform network layer (`howzapp-frontend`) standardizing HTTP APIs (`KtorNetworkClient`), WebSockets (`KtorWebsocketConnector`), and media uploads (`KtorFileUploadClient`).

---

## 3. Technologies Used
- **Backend**: Spring Boot 3, Spring WebFlux, Kotlin Coroutines, Reactive WebClient
- **Polyglot Data Layer**: PostgreSQL (User & conversation metadata), MongoDB (Message histories), Cassandra (High-throughput message logs), Redis (Caching & PubSub)
- **Cloud Media Storage**: AWS S3 SDK (Presigned Uploads & Downloads)
- **Frontend**: Kotlin Multiplatform (KMP), Compose Multiplatform, Ktor Client
- **Build Infrastructure**: Gradle KTS Multi-Project setup (`howzapp-backend`, `howzapp-frontend`, `howzapp-common`)

---

## 4. Testing Technologies
- **Backend Testing**: JUnit 5, MockK, Spring Boot Test
- **Integration Testing**: `ReactiveWebsocketHandlerIntegrationTest` for end-to-end WebSocket framing and Redis event propagation
- **Containerized Integration**: Testcontainers for PostgreSQL, Redis, and MongoDB

---

## 5. Cloud Technologies
- **Storage & CDN**: AWS S3 (Media Asset Bucket Storage & Presigned Authorization)
- **Container Infrastructure**: Docker, Docker Compose
- **Cloud Orchestration**: Kubernetes deployment ready with ingress routing and service meshes

---

## 6. ROADMAP
- [ ] Implement End-to-End Encryption (E2EE) using the Signal Protocol (Double Ratchet Algorithm).
- [ ] Add WebRTC signaling microservice for peer-to-peer audio and video calling.
- [ ] Support message reactions, reply threads, and message editing capability.
- [ ] Integrate Firebase Cloud Messaging (FCM) and Apple Push Notification service (APNs) for offline push notifications.
