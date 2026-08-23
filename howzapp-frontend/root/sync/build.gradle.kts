plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "root.sync"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}