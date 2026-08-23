plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "infra.sync.impl"
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
            api(project(":infra:sync:api"))
            implementation(project(":core:session"))
            implementation(project(":core:network:system"))
            implementation(project(":core:network:websockets"))
        }
    }
}