plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "feature.common.domain.impl"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:common:domain:api"))
            api(project(":core:network:http"))
        }
    }
}
