package com.rommansabbir.cachex.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class CacheXStorageImpl(private var context: Context, private var appName: String) : CacheXStorage {
    private var mSharedPreferences: SharedPreferences =
        context.getSharedPreferences(appName, Context.MODE_PRIVATE)

    override suspend fun doCache(data: String, key: String) {
        getSharedPref().edit {
            putString(key, data)
            apply()
        }
    }

    override suspend fun getCache(key: String): String? {
        return getSharedPref().getString(key, null)
    }

    override fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        this.mSharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override suspend fun clearCacheByKey(key: String): Exception? {
        val data = mSharedPreferences.getString(key, "")
        mSharedPreferences.edit {
            this.remove(key)
            apply()
        }
        return if (data.toString().isEmpty()) Exception("Key not found") else null
    }

    override suspend fun clearAllCache(): Exception? {
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



