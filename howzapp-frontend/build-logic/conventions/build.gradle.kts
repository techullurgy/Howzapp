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