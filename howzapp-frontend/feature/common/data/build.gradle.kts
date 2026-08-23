plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "feature.common.data"
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
            implementation(project(":feature:common:db"))
            implementation(project(":feature:common:domain:api"))
        }
    }
}