plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "base.session"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}