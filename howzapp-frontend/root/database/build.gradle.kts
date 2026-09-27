plugins {
    alias(projectLibs.plugins.conventions.kmp.library)
}

kmpConvention {
    android {
        localNamespace = "root.database"
        hostTest.set(true)
        hostTestConfigure {
            robolectric = true
        }
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

//    testBalloon {
//        enabled = true
//        robolectric = true
//    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.database)
            implementation(projects.feature.users.db)
            implementation(projects.feature.chats.db)

            implementation(app.kotlinx.serialization.json)
        }
    }
}

tasks.withType<Test>().configureEach {
    if(name == "testAndroidHostTest") {
        jvmArgs(
            "--add-opens=java.base/java.lang=ALL-UNNAMED",
            "--add-opens=java.base/java.util=ALL-UNNAMED",
            "--add-opens=java.base/java.io=ALL-UNNAMED",
            "--add-opens=java.base/java.net=ALL-UNNAMED",
            "--add-opens=java.base/java.security=ALL-UNNAMED",
            "--add-opens=java.base/java.text=ALL-UNNAMED",
            "--add-opens=java.base/jdk.internal.access=ALL-UNNAMED",
            "--add-opens=java.desktop/java.awt.font=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
        )
    }
}