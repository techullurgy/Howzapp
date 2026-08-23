package com.techullurgy.conventions.extensions.core

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

open class AndroidConfig @Inject constructor(objects: ObjectFactory) {
    /**
     * Enable Android target
     */
    var enabled: Property<Boolean> = objects.property<Boolean>().convention(true)


    /**
     * Enable Android host tests
     *
     * Creates:
     * androidHostTest source set
     */
    var hostTest: Property<Boolean> = objects.property<Boolean>().convention(false)

    /**
     * Enable Android device tests
     *
     * Creates:
     * androidDeviceTest source set
     */
    var deviceTest: Property<Boolean> = objects.property<Boolean>().convention(false)

    var localNamespace: String = ""

    internal val hostTestConfig = objects.newInstance(HostTestConfig::class.java)
    internal val deviceTestConfig = objects.newInstance(DeviceTestConfig::class.java)

    fun hostTestConfigure(action: Action<HostTestConfig>) {
        action.execute(hostTestConfig)
    }

    fun deviceTestConfigure(action: Action<DeviceTestConfig>) {
        action.execute(deviceTestConfig)
    }

    override fun toString(): String {
        return "AndroidConfig(enabled=${enabled.get()}, hostTest=${hostTest.get()}, deviceTest=${deviceTest.get()}, hostTestConfig=$hostTestConfig, deviceTestConfig=$deviceTestConfig)"
    }
}

open class HostTestConfig {
    var robolectric: Boolean = false
    var roborazzi: Boolean = false

    override fun toString(): String {
        return "HostTestConfig(robolectric=$robolectric, roborazzi=$roborazzi)"
    }
}

open class DeviceTestConfig {
    var composeUiTest: Boolean = false
    var runner: String = "androidx.test.runner.AndroidJUnitRunner"

    override fun toString(): String {
        return "DeviceTestConfig(composeUiTest=$composeUiTest, runner='$runner')"
    }
}