plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "shared"
    }

    ios {
        enabled.set(false)
    }

    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    compose {
        material3 = false
    }

    koin { enabled = true }

    ktor { enabled = true }

    room3 {
        enabled = false
        schemaDir = "$projectDir/schemas"
    }
}