@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
	alias(app.plugins.kotlinMultiplatform)
	alias(app.plugins.kotlin.serialization)
}

group = "com.techullurgy.howzapp"
version = "0.0.1"

kotlin {
	jvm()
	iosArm64()
	iosSimulatorArm64()

	js()
	wasmJs()

	sourceSets {
		commonMain.dependencies {
			implementation(app.kotlinx.serialization.json)
		}
	}
}