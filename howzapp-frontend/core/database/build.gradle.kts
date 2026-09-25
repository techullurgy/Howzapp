plugins {
    alias(projectLibs.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "core.database"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}