package com.rommansabbir.cachex.usecase

import com.rommansabbir.cachex.functional.Either
import com.rommansabbir.cachex.params.GetListParam
import com.rommansabbir.cachex.storage.CacheXStorage
import com.rommansabbir.cachex.worker.CacheXWorkers

class GetListCacheUseCase<T : Any> constructor(
    private val workers: CacheXWorkers,
    private val storage: CacheXStorage
) : UseCase<ArrayList<T>, GetListParam<T>>() {
    override suspend fun run(params: GetListParam<T>): Either<Exception, ArrayList<T>> {
        return workers.getCacheList(params.key, params.clazz, storage)
    }

}