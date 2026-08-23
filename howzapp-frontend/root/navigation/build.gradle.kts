plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "root.navigation"
        hostTest.set(true)
        hostTestConfigure {
            robolectric = true
        }
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    compose {
        enabled = true
        ui = true
        foundation = true
        preview = true
    }

    navigation3 {
        enabled = true
        ui = true
    }

    koin {
        enabled = true
        compose = true
        navigation3 = true
    }

    kmp { serialization = true }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:navigation"))
        }
    }
}