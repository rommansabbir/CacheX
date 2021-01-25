package com.rommansabbir.cachex.usecase

import com.rommansabbir.cachex.functional.Either
import com.rommansabbir.cachex.params.GetSingleParam
import com.rommansabbir.cachex.storage.CacheXStorage
import com.rommansabbir.cachex.worker.CacheXWorkers

class GetSingleCacheUseCase<T : Any> constructor(
    private var worker: CacheXWorkers,
    private var stroage: CacheXStorage
) : UseCase<T, GetSingleParam<T>>() {
    override suspend fun run(params: GetSingleParam<T>): Either<Exception, T> {
        return worker.getCacheSingle(params.key, params.clazz, stroage)
    }
}