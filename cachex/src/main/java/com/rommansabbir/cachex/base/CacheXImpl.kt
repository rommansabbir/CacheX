package com.rommansabbir.cachex.base

import com.rommansabbir.cachex.callback.CacheXCallback
import com.rommansabbir.cachex.functional.Either
import com.rommansabbir.cachex.storage.CacheXStorage
import com.rommansabbir.cachex.worker.CacheXWorkers

class CacheXImpl constructor(
    private var cacheXStorage: CacheXStorage,
    private var cacheXWorkers: CacheXWorkers
) : CacheX {
    override val listenersList: HashMap<String, CacheXCallback> = HashMap()

    override suspend fun <T> getCacheSingle(key: String, clazz: Class<T>): Either<Exception, T> {
        return cacheXWorkers.getCacheSingle(key, clazz, cacheXStorage)
    }

    override suspend fun <T> getCacheList(
        key: String,
        clazz: Class<T>
    ): Either<Exception, ArrayList<T>> {
        return cacheXWorkers.getCacheList(key, clazz, cacheXStorage)
    }

    override suspend fun <T> cacheSingle(key: String, data: T): Either<Exception, Boolean> {
        return cacheXWorkers.cacheSingle(key, data, cacheXStorage)
    }

    override suspend fun <T> cacheList(
        key: String,
        data: List<T>
    ): Either<Exception, Boolean> {
        return cacheXWorkers.cacheList(key, data, cacheXStorage)
    }

    override fun registerListener(callback: CacheXCallback, key: String) {
        listenersList[key] = callback
    }

    override fun registerListener(callback: CacheXCallback, key: ArrayList<String>) {
        key.forEach {
            listenersList[it] = callback
        }
    }

    override fun unregisterListener(key: String) {
        listenersList.remove(key)
    }

    override fun unregisterListener(key: ArrayList<String>) {
        key.forEach {
            listenersList.remove(it)
        }
    }

    override fun clearListeners() {
        listenersList.clear()
    }

    override suspend fun clearCacheByKey(key: String): Exception? {
        return cacheXStorage.clearCacheByKey(key)
    }

    override suspend fun clearAllCache(): Exception? {
        return cacheXStorage.clearAllCache()
    }
}