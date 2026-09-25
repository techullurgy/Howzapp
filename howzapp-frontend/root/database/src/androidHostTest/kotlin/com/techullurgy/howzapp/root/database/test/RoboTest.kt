package com.techullurgy.howzapp.root.database.test

import com.techullurgy.howzapp.root.database.test.roomShared.howzappDatabase
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class RoboTest {
    @Test
    fun test() {
        val database = howzappDatabase()
    }
}