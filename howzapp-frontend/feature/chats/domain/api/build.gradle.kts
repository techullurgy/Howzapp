plugins {
    alias(projectLibs.plugins.conventions.kmp.library)
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
            api(projects.feature.users.domain.api)
            api(projects.core.domain)
            api(app.androidx.paging3.common)
        }
    }
}