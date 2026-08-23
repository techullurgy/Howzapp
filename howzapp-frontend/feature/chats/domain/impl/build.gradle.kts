plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "feature.chats.domain.impl"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    koin {
        enabled = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":feature:chats:domain:api"))
            implementation(project(":feature:common:domain:api"))
        }
    }
}