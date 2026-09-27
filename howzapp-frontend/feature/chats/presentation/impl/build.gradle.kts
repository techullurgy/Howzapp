plugins {
    alias(projectLibs.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "feature.chats.presentation.impl"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    koin {
        enabled = true
        compose = true
        composeViewmodel = true
        navigation3 = true
    }

    navigation3 {
        enabled = true
    }

    compose {
        enabled = true
        foundation = true
        ui = true
    }

    kmp {
        datetime = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.presentation)
            implementation(projects.feature.chats.presentation.api)
            implementation(projects.feature.chats.domain.api)

            implementation(app.androidx.paging3.compose)
        }
    }
}