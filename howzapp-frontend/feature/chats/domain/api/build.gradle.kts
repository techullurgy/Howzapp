plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "feature.chats.domain.api"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:users:domain:api"))
            api(app.androidx.paging3.common)
        }
    }
}