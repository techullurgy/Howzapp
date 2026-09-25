plugins {
    alias(app.plugins.kotlinMultiplatform) apply false
    alias(app.plugins.androidApplication) apply false
    alias(app.plugins.androidKmpLibrary) apply false
    alias(app.plugins.composeMultiplatform) apply false
    alias(app.plugins.composeCompiler) apply false
    alias(app.plugins.kotlin.serialization) apply false
    alias(app.plugins.koinCompiler) apply false
    alias(app.plugins.ksp) apply false
    alias(app.plugins.room3) apply false
    alias(app.plugins.testBalloon) apply false

    alias(projectLibs.plugins.conventions.kmp.library) apply false
}