package com.rommansabbir.cachex.base

import com.rommansabbir.cachex.callback.CacheXCallback
import com.rommansabbir.cachex.functional.Either

interface CacheX {
    /**
     * To store the list of listeners to notify on data changes using callback function.
     *
     * It's a [HashMap] which takes [String] as it's key & [CacheXCallback] as the value.
     *
     * When any data changes occur, registered listeners will get notified based on the key.
     */
    val listenersList: HashMap<String, CacheXCallback>

    /**
     * To cache a single data which is generic [T].
     *
     * First, encrypt the data and then cache the data by following the key.
     *
     * @param key The key to cache the data which is [String]
     * @param data The required data that need to be cached which is generic [T]
     *
     * @return Either<Exception, Boolean>
     */
    suspend fun <T> cacheSingle(key: String, data: T): Either<Exception, Boolean>

    /**
     * To get a single generic ([T]) cached data from the cache.
     *
     * First, get the data from the cache using the key,
     * then decrypt the data and return the data.
     *
     * @param key The key which was used to cache the data
     * @param clazz The data type which will be returned on success
     *
     * @return Either<Exception, T>
     */
    suspend fun <T> getCacheSingle(key: String, clazz: Class<T>): Either<Exception, T>

    /**
     * To cache a list data which is generic [T].
     *
     * First, encrypt the data and then cache the data by following the key.
     *
     * @param key The key to cache the data which is [String]
     * @param data The required data that need to be cached which is generic [T]
     *
     * @return Either<Exception, Boolean>
     */
    suspend fun <T> cacheList(key: String, data: List<T>): Either<Exception, Boolean>

    /**
     * To get a list of generics ([T]) cached data from the cache.
     *
     * First, get the data from the cache using the key,
     * then decrypt the data and return the data.
     *
     * @param key The key which was used to cache the data
     * @param clazz The data type which will be returned on success
     *
     * @return Either<Exception, ArrayList<T>>
     */
    suspend fun <T> getCacheList(key: String, clazz: Class<T>): Either<Exception, ArrayList<T>>

    /**
     * Register a listener to get notified on data changes.
     *
     * @param callback Listener instance
     * @param key The subscription key to get notified on data changes
     */
    fun registerListener(callback: CacheXCallback, key: String)

    /**
     * Register a list of listeners to get notified on data changes.
     *
     * @param callback Listener instance
     * @param key The list of subscription key to get notified on data changes
     */
    fun registerListener(callback: CacheXCallback, key: ArrayList<String>)

    /**
     * Unregister a listener from the listeners list.
     *
     * @param key The subscription key to unregister
     */
    fun unregisterListener(key: String)

    /**
     * Unregister a list of listeners from the listeners list.
     *
     * @param key The list of subscriptions key to unregister
     */
    fun unregisterListener(key: ArrayList<String>)

    /**
     * Clear all listeners from the listeners list.
     */
    fun clearListeners()

    /**
     * Clear stored data by following the respective key.
     *
     * @param key The which was used to store the data
     *
     * @return [Exception], Exception can be null
     */
    suspend fun clearCacheByKey(key: String): Exception?

    /**
     * Clear all stored data.
     *
     * @return [Exception], Exception can be null
     */
    suspend fun clearAllCache(): Exception?
}