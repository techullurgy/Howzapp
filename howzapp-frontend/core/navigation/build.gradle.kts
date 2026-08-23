plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "core.navigation"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(app.androidx.navigation3.runtime)
        }
    }
}