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

    testImplementation(server.spring.boot.starter.test)
    testImplementation(server.kotlin.test.junit5)
    testRuntimeOnly(server.junit.platform.launcher)
    testImplementation(server.testcontainers.junit.jupiter)
    testImplementation(server.testcontainers.redis)
}

tasks.withType<Test> {
    useJUnitPlatform()
}