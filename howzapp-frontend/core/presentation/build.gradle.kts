plugins {
    alias(projectLibs.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "core.presentation"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    navigation3 {
        enabled = true
    }

    compose {
        enabled = true
        foundation = true
        ui = true
        material3 = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(app.coil.compose)
            implementation(app.coil.ktor3)
            implementation(app.ktor.client.cio)
        }
    }
}
