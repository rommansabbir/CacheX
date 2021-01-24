package com.rommansabbir.cachexdemo

import android.app.Application
import com.rommansabbir.cachex.core.CacheXCore

class MyApplication : Application() {
    private val encryptionKey = "!x@4#w$%f^g&h*8(j)9b032ubfu8238!"

    override fun onCreate() {
        super.onCreate()
        CacheXCore.init(this, encryptionKey, getString(R.string.app_name))
    }
}