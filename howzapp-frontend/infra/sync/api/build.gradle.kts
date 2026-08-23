plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "infra.sync.api"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
        }
    }
}