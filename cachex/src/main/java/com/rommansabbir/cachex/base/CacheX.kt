package com.rommansabbir.cachex.base

import com.rommansabbir.cachex.callback.CacheXCallback
import com.rommansabbir.cachex.usecase.GetListCacheUseCase
import com.rommansabbir.cachex.usecase.GetSingleCacheUseCase
import com.rommansabbir.cachex.usecase.ListCacheUseCase
import com.rommansabbir.cachex.usecase.SingleCacheUseCase

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
     * To cache single data which is generic [T].
     *
     * First, encrypt the data and then cache the data by following the key.
     *
     * @return SingleCacheUseCase<T>
     */
    fun <T> cacheSingle(): SingleCacheUseCase<T>

    /**
     * To get single generic ([T]) cached data from the cache.
     *
     * First, get the data from the cache using the key,
     * then decrypt the data and return the data.
     *
     * @return GetSingleCacheUseCase<T>
     */
    fun <T : Any> getCacheSingle(): GetSingleCacheUseCase<T>

    /**
     * To cache a list data which is generic [T].
     *
     * First, encrypt the data and then cache the data by following the key.
     *
     * @return ListCacheUseCase<T>
     */
    fun <T> cacheList(): ListCacheUseCase<T>

    /**
     * To get a list of generics ([T]) cached data from the cache.
     *
     * First, get the data from the cache using the key,
     * then decrypt the data and return the data.
     *
     * @return GetListCacheUseCase<T>
     */
    fun <T : Any> getCacheList(): GetListCacheUseCase<T>

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
     */
    fun clearCacheByKey(key: String)

    /**
     * Clear all stored data.
     *
     */
    fun clearAllCache()
}