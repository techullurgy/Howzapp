package com.techullurgy.conventions.extensions.core

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

open class IosConfig @Inject constructor(objects: ObjectFactory) {
    val enabled: Property<Boolean> = objects.property<Boolean>()
        .convention(true)

    val frameworkName: Property<String> = objects.property<String>()

    val isStatic: Property<Boolean> = objects.property<Boolean>()
        .convention(
            frameworkName.map { true }
        )
}