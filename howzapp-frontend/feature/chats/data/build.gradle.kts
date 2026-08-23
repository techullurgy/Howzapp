plugins {
    alias(project.plugins.conventions.kmp.library)
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
            api(project(":feature:chats:domain:api"))
            implementation(project(":feature:chats:db"))
            implementation(project(":core:database"))

            implementation("com.techullurgy.howzapp:howzapp-common:0.0.1")
        }
        commonTest.dependencies {
            implementation(app.androidx.paging3.testing)
        }
    }
}