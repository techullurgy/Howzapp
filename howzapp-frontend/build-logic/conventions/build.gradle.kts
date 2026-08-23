import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

private val groupNamePrefix = projectLibs.versions.projectApplicationId.get()
private val javaVersion = projectLibs.versions.javaVersion.get()

group = "$groupNamePrefix.buildlogic.convention"

dependencies {
    compileOnly(appLibs.android.gradlePlugin)
    compileOnly(appLibs.android.tools.common)
    compileOnly(appLibs.kotlin.gradlePlugin)
    compileOnly(appLibs.compose.compiler.gradlePlugin)
    compileOnly(appLibs.compose.multiplatform.gradlePlugin)
    compileOnly(appLibs.kotlin.multiplatform.gradlePlugin)
    compileOnly(appLibs.ksp.gradlePlugin)
    compileOnly(appLibs.koin.compiler.gradlePlugin)
    compileOnly(appLibs.androidx.room.gradle.plugin)
    compileOnly(appLibs.androidx.room3.gradle.plugin)
}

java {
    val javaVersionInt = javaVersion.toInt()
    sourceCompatibility = JavaVersion.toVersion(javaVersionInt)
    targetCompatibility = JavaVersion.toVersion(javaVersionInt)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.set(listOf("-Xcontext-parameters"))
        jvmTarget = JvmTarget.fromTarget(javaVersion)
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("kmpLibraryConvention") {
            id = "conventions.kmp.library"
            implementationClass = "com.techullurgy.conventions.plugins.KmpConventionPlugin"
        }
    }
}

//gradlePlugin {
//    plugins {
//        register("androidApplication") {
//            id = "$groupNamePrefix.conventions.android.application"
//            implementationClass = "AndroidApplicationConventionPlugin"
//        }
//        register("androidComposeApplication") {
//            id = "$groupNamePrefix.conventions.android.application.compose"
//            implementationClass = "AndroidApplicationComposeConventionPlugin"
//        }
//        register("kmpLibrary") {
//            id = "$groupNamePrefix.conventions.kmp.library"
//            implementationClass = "KmpLibraryConventionPlugin"
//        }
//        register("cmpLibrary") {
//            id = "$groupNamePrefix.conventions.cmp.library"
//            implementationClass = "CmpLibraryConventionPlugin"
//        }
//        register("buildKonfig") {
//            id = "$groupNamePrefix.conventions.buildkonfig"
//            implementationClass = "BuildKonfigConventionPlugin"
//        }
//        register("room") {
//            id = "$groupNamePrefix.conventions.room"
//            implementationClass = "RoomConventionPlugin"
//        }
//        register("koinCompiler") {
//            id = "$groupNamePrefix.conventions.koin.compiler"
//            implementationClass = "KoinAnnotationsConventionPlugin"
//        }
//    }
//}