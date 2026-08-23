import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(app.plugins.androidApplication)
    alias(app.plugins.composeCompiler)
    alias(app.plugins.koinCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {

    implementation(project(":root:database:core"))
    implementation(project(":feature:chats:di"))
    implementation(project(":feature:chats:presentation:api"))

    implementation(app.androidx.activity.compose)

    implementation(app.compose.ui.tooling.preview)
    debugImplementation(app.compose.ui.tooling)

    implementation(platform(app.koin.bom))
    implementation(app.koin.core)
    implementation(app.koin.annotations)
    implementation(app.koin.androidx.compose)
    implementation(app.koin.compose.navigation3)

    implementation(app.androidx.navigation3.runtime)
    implementation(app.androidx.navigation3.ui)
}

android {
    namespace = "com.techullurgy.howzapp"
    compileSdk = app.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.techullurgy.howzapp"
        minSdk = app.versions.android.minSdk.get().toInt()
        targetSdk = app.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }

    // Tell AGP to use the release build for androidTests
    // testBuildType = "release"
}

koinCompiler {

}