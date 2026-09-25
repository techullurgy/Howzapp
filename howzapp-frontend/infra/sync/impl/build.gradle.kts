plugins {
    alias(projectLibs.plugins.conventions.kmp.library)
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
            implementation(projectLibs.howzapp.common)
            api(projects.infra.sync.api)
            implementation(projects.core.session)
            implementation(projects.core.network.system)
            implementation(projects.core.network.websockets)
        }
    }
}