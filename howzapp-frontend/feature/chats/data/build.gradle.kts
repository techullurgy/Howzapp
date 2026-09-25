plugins {
    alias(projectLibs.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "feature.chats.data"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    room3 {
        enabled = true
    }

    koin {
        enabled = true
    }

    kmp {
        datetime = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.feature.chats.domain.api)
            implementation(projects.feature.chats.db)
            implementation(projects.core.database)
            implementation(projects.core.network.fileupload)
            implementation(projects.infra.sync.api)

            implementation(projectLibs.howzapp.common)
        }
        commonTest.dependencies {
            implementation(app.androidx.paging3.testing)
        }
    }
}