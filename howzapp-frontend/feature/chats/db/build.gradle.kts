plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "feature.chats.db"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    room3 {
        enabled = true
    }

    kmp {
        serialization = true
    }
}