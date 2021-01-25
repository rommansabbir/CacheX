package com.rommansabbir.cachex.storage

import android.content.SharedPreferences

interface CacheXStorage {
    /**
     * Cache single data.
     *
     * @param data The data which will be stored
     * @param key The key to store the data
     */
    suspend fun doCache(data: String, key: String)

    /**
     * Get single stored data.
     *
     * @param key The which was used to store the data
     *
     * @return [String], Data can be null
     */
    suspend fun getCache(key: String): String?

    /**
     * Register listener to get notified on data changes.
     *
     * @param listener Listener to listen to the changes
     */
    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)

    /**
     * Clear stored data by following the respective key.
     *
     * @param key The which was used to store the data
     */
    fun clearCacheByKey(key: String)

    /**
     * Clear all stored data.
     *
     */
    fun clearAllCache()
}