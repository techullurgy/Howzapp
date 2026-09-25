plugins {
    alias(projectLibs.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "core.network.http"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}