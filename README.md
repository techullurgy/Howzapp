# Howzapp

**Howzapp** is a modern, cross-platform messaging platform inspired by WhatsApp, designed to deliver a rich, real-time communication experience across **Android**, **iOS**, and **Desktop**. The project consists of a **Kotlin Multiplatform client application** and a **cloud-native microservices backend**, showcasing end-to-end application architecture from user interface to distributed backend services.

Built with a strong focus on scalability, reliability, and developer experience, Howzapp explores real-world messaging challenges including real-time communication, media sharing, push notifications, offline synchronization, and cloud-native deployment.

> **Current Platform Support:** Android is the primary target platform. iOS and Desktop share the majority of the codebase, with some platform-specific features still under development.

---

# Features

## Messaging

- One-to-one conversations.
- Group chats with multiple participants.
- Community channels where administrators broadcast messages to subscribers.
- Rich messaging experience with support for multiple content types.
- Real-time message delivery and synchronization.

### Supported Message Types

- Plain text messages.
- Images.
- Videos.
- Audio files.
- Voice recordings.
- Video recordings.
- Mixed messages combining media and text.

---

## Presence & Activity

Real-time user presence powered by persistent network connections.

Supported presence states include:

- Online
- Last Seen
- Typing...
- Recording Audio
- Recording Video

These states are synchronized across connected clients to provide an interactive messaging experience.

---

## Status Updates

Users can publish temporary status updates including:

- Text
- Images
- Videos
- Audio

---

## Calls *(In Progress)*

Voice and video calling capabilities are currently under development.

Planned technologies include:

- WebRTC
- Stream Video SDK

---

## Connectivity

The application actively monitors network availability and provides users with real-time connection status, improving transparency during offline or unstable network conditions.

---

# Client Architecture

The client application follows a modular, clean architecture with a shared codebase across supported platforms.

## Core Technologies

### Kotlin Multiplatform (KMP)

- Shared business logic.
- Shared networking.
- Shared domain layer.
- Shared data layer.

### Compose Multiplatform (CMP)

- Shared declarative UI.
- Responsive layouts for phones, tablets, and desktop.
- Platform-adaptive navigation and interactions.

### Dependency Injection

- Koin

### Navigation

- Jetpack Navigation 3
- Type-safe navigation
- Modular navigation graphs

### Networking

- Ktor Client
- WebSockets for real-time messaging
- Coroutines & Flows
- Interactive upload progress tracking

### Push Notifications

- Firebase Cloud Messaging (FCM)

### Media

Android

- Media3
- CameraX
- WorkManager

Apple Platforms

- AVPlayer

---

# Technical Highlights

The client application demonstrates a wide range of modern mobile development techniques including:

- Real-time messaging using WebSockets.
- Background media uploads.
- Progressive upload status updates.
- Cross-platform shared architecture.
- Offline-aware UI.
- Responsive layouts.
- Platform-specific media integrations.
- Modular feature organization.
- State-driven Compose UI.
- Clean Architecture principles.

---

# Backend Architecture

The backend is implemented as a collection of **Spring Boot microservices**, designed for scalability, resilience, and cloud-native deployment.

## Core Technologies

- Kotlin
- Spring Boot
- Spring Cloud Stream
- Apache Kafka
- Docker
- Kubernetes
- Helm Charts
- GitHub Actions
- SQL databases
- NoSQL databases

Each microservice is independently deployable and communicates asynchronously through Kafka, enabling reliable event-driven workflows for messaging and notifications.

---

# Cloud-Native Infrastructure

The backend is designed with production-ready deployment practices.

Highlights include:

- Containerized services using Docker.
- Kubernetes orchestration.
- Helm-based deployments.
- CI/CD pipelines using GitHub Actions.
- Environment-specific configuration.
- Scalable microservice architecture.
- Event-driven communication.

---

# Testing Strategy

The project emphasizes automated testing across both client and server components.

## Client Testing

- Compose UI Testing
- Robolectric
- Roborazzi Screenshot Testing
- WireMock for API mocking

## Backend Testing

- Testcontainers
- WireMock
- Integration Testing
- Microservice Testing

---

# Skills Demonstrated

### Mobile Development

- Kotlin Multiplatform
- Compose Multiplatform
- Android Development
- iOS Development
- Desktop Development
- Responsive UI Design
- Media Processing
- Camera Integration
- Background Processing
- Push Notifications
- Real-Time Communication
- WebSockets
- Dependency Injection
- Navigation Architecture
- Multi-Module Architecture
- Clean Architecture

### Backend Development

- Kotlin
- Spring Boot
- Microservices Architecture
- Event-Driven Systems
- Apache Kafka
- REST APIs
- SQL & NoSQL Databases
- Docker
- Kubernetes
- Helm
- CI/CD Pipelines
- Cloud-Native Development
- Automated Testing