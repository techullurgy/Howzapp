plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {}
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    ktor {
        enabled = true
        auth = true
        websocket = true
    }

    koin {
        enabled = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:network:http"))
            api(project(":core:network:websockets"))
            implementation(project(":core:session"))
        }
    }
}