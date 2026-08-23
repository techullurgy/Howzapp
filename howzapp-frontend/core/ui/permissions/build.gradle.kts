plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {}
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    compose { enabled = true }
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(app.androidx.activity.compose)
            }
        }
        commonMain {
            dependencies {
//                implementation(project(":core:files"))
            }
        }
        webMain {
            dependencies {
                implementation(app.wrappers.browser)
            }
        }
    }
}