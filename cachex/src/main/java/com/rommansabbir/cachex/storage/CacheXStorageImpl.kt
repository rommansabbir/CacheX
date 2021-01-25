package com.rommansabbir.cachex.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class CacheXStorageImpl(context: Context, appName: String) : CacheXStorage {
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

    override fun clearCacheByKey(key: String) {
        mSharedPreferences.edit().remove(key).apply()
    }

    override fun clearAllCache() {
        mSharedPreferences.edit().clear().apply()
    }

    private fun getSharedPref(): SharedPreferences {
        return mSharedPreferences
    }

}



