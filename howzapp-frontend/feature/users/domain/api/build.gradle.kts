plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "feature.users.domain.api"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}