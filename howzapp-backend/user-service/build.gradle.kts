
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

    // Caffeine Cache library
    implementation(server.caffeine)

    implementation(server.spring.boot.starter.data.r2dbc)
    implementation(server.r2dbc.h2)

    testImplementation(server.spring.boot.starter.test)
    testImplementation(server.kotlin.test.junit5)
    testRuntimeOnly(server.junit.platform.launcher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}