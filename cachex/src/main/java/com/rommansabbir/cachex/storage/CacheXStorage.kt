package com.rommansabbir.cachex.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class CacheXStorage(private var context: Context): CacheXCacheLogic{

    /**
     * Save data to shared pref into key value pair
     *
     * @param data, data that need to be saved to shared pref in [String] format
     * @param key, key that need to be used to save the passed [data] in [String] format
     */
    override fun doCache(data: String, key: String) {
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

    /**
     * Provide Shared Preference reference
     *
     * @param context, pass [context] to instantiate [SharedPreferences] instance reference
     *
     * @return [SharedPreferences], it will return an [SharedPreferences] instance reference
     */
    private fun getSharedPref(context: Context): SharedPreferences =
        context.getSharedPreferences("CacheX", Context.MODE_PRIVATE)
}

interface CacheXCacheLogic{
    fun doCache(data: String, key: String)
    fun getCache(key: String): String?
}

fun getEmitterException(message: String = "Emitter has been disposed") = Exception(message)
