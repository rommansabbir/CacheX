package com.rommansabbir.cachexdemo

import android.app.Application
import com.rommansabbir.cachex.CacheX

class MyApplication : Application() {
    private val encryptionKey = "!x@4#w$%f^g&h*8(j)9b032ubfu8238!"

    override fun onCreate() {
        super.onCreate()
        CacheX.initializeComponents(encryptionKey)
    }
}