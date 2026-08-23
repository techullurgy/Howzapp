plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {}
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}

kotlin {
    sourceSets {
        webMain {
            dependencies {
                implementation(app.wrappers.browser)
            }
        }
    }
}