plugins {
    alias(projectLibs.plugins.conventions.kmp.library)
}

kmpConvention {
    android {}
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }
}