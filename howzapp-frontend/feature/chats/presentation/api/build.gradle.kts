plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "feature.chats.presentation.api"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    kmp {
        serialization = true
    }

    compose {
        enabled = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:navigation"))
        }
    }
}