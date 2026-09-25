plugins {
    alias(projectLibs.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "base.network"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    ktor {
        enabled = true
        websocket = true
        auth = true
    }

    koin {
        enabled = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projectLibs.howzapp.common)
            implementation(projects.core.qualifiers)
            implementation(projects.core.network.http)
            implementation(projects.core.network.websockets)
            implementation(projects.core.network.fileupload)
        }
    }
}