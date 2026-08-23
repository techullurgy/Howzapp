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
        commonMain.dependencies {
            api(project(":core:domain"))
            api(project(":core:files"))

            implementation(app.kotlinx.io.core)
        }
    }
}