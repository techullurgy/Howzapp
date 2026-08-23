plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "feature.chats.di"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    koin {
        enabled = true
        composeViewmodel = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":feature:chats:presentation:impl"))
//            implementation(project(":feature:chats:domain:impl"))
            implementation(project(":feature:chats:data"))
        }
    }
}