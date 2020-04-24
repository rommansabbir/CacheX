package com.rommansabbir.cachex.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class CacheXStorage(private var context: Context) : CacheXCacheLogic {
    private lateinit var mSharedPreferences: SharedPreferences

    override suspend fun doCache(data: String, key: String) {
        getSharedPref(context).edit {
            putString(key, data)
            apply()
        }
    }

    /**
     * Get saved data from shared pref using key value pair
     *
     * @param key, key that were used to save data in shared pref in [String] format
     *
     * @return [String], it will return string or null string
     */
    override fun getCache(key: String): String? {
        return getSharedPref(context).getString(key, null)
    }

    override fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        this.mSharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    /**
     * Provide Shared Preference reference
     *
     * @param context, pass [context] to instantiate [SharedPreferences] instance reference
     *
     * @return [SharedPreferences], it will return an [SharedPreferences] instance reference
     */
    private fun getSharedPref(context: Context): SharedPreferences {
        mSharedPreferences = context.getSharedPreferences("CacheX", Context.MODE_PRIVATE)
        return mSharedPreferences
    }

}

interface CacheXCacheLogic {
    suspend fun doCache(data: String, key: String)
    fun getCache(key: String): String?
    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)
}

fun getEmitterException(message: String = "Emitter has been disposed") = Exception(message)
