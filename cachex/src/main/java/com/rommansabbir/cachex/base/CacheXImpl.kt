package com.rommansabbir.cachex.base

import com.rommansabbir.cachex.callback.CacheXCallback
import com.rommansabbir.cachex.storage.CacheXStorage
import com.rommansabbir.cachex.usecase.GetListCacheUseCase
import com.rommansabbir.cachex.usecase.GetSingleCacheUseCase
import com.rommansabbir.cachex.usecase.ListCacheUseCase
import com.rommansabbir.cachex.usecase.SingleCacheUseCase
import com.rommansabbir.cachex.worker.CacheXWorkers

class CacheXImpl constructor(
    private var cacheXStorage: CacheXStorage,
    private var cacheXWorkers: CacheXWorkers
) : CacheX {
    override val listenersList: HashMap<String, CacheXCallback> = HashMap()


    override fun <T> cacheSingle(): SingleCacheUseCase<T> {
        return SingleCacheUseCase(cacheXWorkers, cacheXStorage)
    }

    override fun <T : Any> getCacheSingle(): GetSingleCacheUseCase<T> {
        return GetSingleCacheUseCase(cacheXWorkers, cacheXStorage)
    }

    override fun <T> cacheList(): ListCacheUseCase<T> {
        return ListCacheUseCase(cacheXWorkers, cacheXStorage)
    }

    override fun <T : Any> getCacheList(): GetListCacheUseCase<T> {
        return GetListCacheUseCase(cacheXWorkers, cacheXStorage)
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

    override fun clearCacheByKey(key: String) {
        return cacheXStorage.clearCacheByKey(key)
    }

    override fun clearAllCache() {
        return cacheXStorage.clearAllCache()
    }
}