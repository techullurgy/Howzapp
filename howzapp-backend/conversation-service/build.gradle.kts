plugins {
    alias(server.plugins.kotlinJvmPlugin)
    alias(server.plugins.springPlugin)
    alias(server.plugins.springBootPlugin)
    alias(server.plugins.springDependencyManagementPlugin)
}

group = "com.techullurgy.howzapp"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {

    implementation(project(":common"))

    implementation(server.bundles.webflux.kotlin)
    implementation(server.spring.boot.starter.kotlinx.serialization.json)
    implementation(server.kotlin.reflect)
    implementation(server.jackson.module.kotlin)
    implementation(server.caffeine)
    implementation(server.kafka.reactor)
    implementation(server.spring.boot.starter.data.cassandra.reactive)

    testImplementation(server.spring.boot.starter.test)
    testImplementation(server.kotlin.test.junit5)
    testRuntimeOnly(server.junit.platform.launcher)
    testImplementation(server.testcontainers.junit.jupiter)

    implementation(platform(server.spring.cloud.bom))
    implementation(server.spring.cloud.stream)
    implementation(server.spring.cloud.stream.binder.kafka)
}

tasks.withType<Test> {
    useJUnitPlatform()
}