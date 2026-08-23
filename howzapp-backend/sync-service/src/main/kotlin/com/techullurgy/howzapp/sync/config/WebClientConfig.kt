package com.techullurgy.howzapp.sync.config

import com.techullurgy.howzapp.sync.clients.ConversationServiceClient
import com.techullurgy.howzapp.sync.clients.StatusServiceClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class WebClientConfig {

    @Bean
    fun conversationServiceClient(
        @Value("\${services.conversation.url:http://localhost:8081}") baseUrl: String
    ): ConversationServiceClient {
        // Non-blocking WebClient instance
        val webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build()

        // Adapt WebClient to Declarative HTTP Interface
        val adapter = WebClientAdapter.create(webClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()

        return factory.createClient(ConversationServiceClient::class.java)
    }

    @Bean
    fun statusServiceClient(
        @Value("\${services.status.url:http://localhost:8082}") baseUrl: String
    ): StatusServiceClient {
        val webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build()

        val adapter = WebClientAdapter.create(webClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()

        return factory.createClient(StatusServiceClient::class.java)
    }
}