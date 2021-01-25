package com.rommansabbir.cachex.usecase

import com.rommansabbir.cachex.functional.Either
import com.rommansabbir.cachex.params.ListParam
import com.rommansabbir.cachex.storage.CacheXStorage
import com.rommansabbir.cachex.worker.CacheXWorkers

class ListCacheUseCase<T> constructor(
    private val workers: CacheXWorkers,
    private val storage: CacheXStorage
) : UseCase<Boolean, ListParam<T>>() {
    override suspend fun run(params: ListParam<T>): Either<Exception, Boolean> {
        return workers.cacheList(params.key, params.data, storage)
    }
}