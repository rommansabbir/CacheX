package com.rommansabbir.cachex.worker

import com.rommansabbir.cachex.functional.Either
import com.rommansabbir.cachex.storage.CacheXStorage

interface CacheXWorkers {
    /**
     * Cache a single data.
     *
     * @param key The key cache the data
     * @param data The data ([T]) which need to be cached
     * @param xStorage Storage reference to store the data
     *
     * @return Either<Exception, Boolean>
     */
    suspend fun <T> cacheSingle(
        key: String,
        data: T,
        xStorage: CacheXStorage
    ): Either<Exception, Boolean>

    /**
     * Get a single data from the cache.
     *
     * @param key The key cache the data
     * @param clazz The required data that need to be cached which is generic [T]
     * @param xCacheXStorage Storage reference to get the stored data
     *
     * @return Either<Exception, T>
     */
    suspend fun <T> getCacheSingle(
        key: String,
        clazz: Class<T>,
        xCacheXStorage: CacheXStorage
    ): Either<Exception, T>

    /**
     * Cache a  list of data.
     *
     * @param key The key cache the data
     * @param data The data list ([T]) which need to be cached
     * @param xCacheXStorage Storage reference to store the data
     *
     * @return Either<Exception, Boolean>
     */
    suspend fun <T> cacheList(
        key: String,
        data: List<T>,
        xCacheXStorage: CacheXStorage
    ): Either<Exception, Boolean>

    /**
     * Get a list data from the cache.
     *
     * @param key The key cache the data
     * @param clazz The required data that need to be cached which is generic [T]
     * @param xCacheXStorage Storage reference to get the stored data
     *
     * @return Either<Exception, ArrayList<T>>
     */
    suspend fun <T> getCacheList(
        key: String,
        clazz: Class<T>,
        xCacheXStorage: CacheXStorage
    ): Either<Exception, ArrayList<T>>

}