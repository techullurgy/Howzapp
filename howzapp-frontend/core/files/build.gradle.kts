plugins {
    alias(projectLibs.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "core.files"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation("androidx.startup:startup-runtime:1.2.0")
            }
        }
        webMain {
            dependencies {
                implementation(app.wrappers.browser)
            }
        }
    }
}