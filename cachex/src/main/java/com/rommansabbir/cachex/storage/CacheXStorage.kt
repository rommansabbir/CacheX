package com.rommansabbir.cachex.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class CacheXStorage(private var context: Context) : CacheXCacheLogic {
    private var mSharedPreferences: SharedPreferences =
        context.getSharedPreferences("CacheX", Context.MODE_PRIVATE)

    override suspend fun doCache(data: String, key: String) {
        getSharedPref().edit {
            putString(key, data)
            apply()
        }
    }

    override fun getCache(key: String): String? {
        return getSharedPref().getString(key, null)
    }

    override fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        this.mSharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun clearCacheByKey(key: String): Exception? {
        val data = mSharedPreferences.getString(key, "")
        mSharedPreferences.edit {
            this.remove(key)
            apply()
        }
        return if (data.toString().isEmpty()) Exception("Key not found") else null
    }

    override fun clearAllCache(): Exception? {
        return try {
            mSharedPreferences.edit().clear().apply()
            null
        } catch (e: Exception) {
            e
        }
    }

    private fun getSharedPref(): SharedPreferences {
        return mSharedPreferences
    }

}

interface CacheXCacheLogic {
    suspend fun doCache(data: String, key: String)
    fun getCache(key: String): String?
    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)
    fun clearCacheByKey(key: String): Exception?
    fun clearAllCache(): Exception?
}

fun getEmitterException(message: String = "Emitter has been disposed") = Exception(message)
