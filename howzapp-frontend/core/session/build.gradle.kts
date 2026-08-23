plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "core.session"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}