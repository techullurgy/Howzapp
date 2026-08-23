plugins {
    alias(project.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "root.database"
    }
    ios {}
    jvm {}
    js { enabled = true }
    wasm { enabled = true }

    room3 {
        enabled = true
        compiler = true
        schemaDir = "$projectDir/schemas"
    }

    koin {
        enabled = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:database"))
            implementation(project(":feature:common:db"))
            implementation(project(":feature:chats:db"))
        }
    }
}