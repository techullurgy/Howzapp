plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "core.network.system"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}