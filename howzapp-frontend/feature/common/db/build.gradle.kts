plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "feature.common.db"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    room3 {
        enabled = true
    }
}
