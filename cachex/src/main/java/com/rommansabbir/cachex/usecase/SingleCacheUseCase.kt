package com.rommansabbir.cachex.usecase

import com.rommansabbir.cachex.functional.Either
import com.rommansabbir.cachex.params.SingleParam
import com.rommansabbir.cachex.storage.CacheXStorage
import com.rommansabbir.cachex.worker.CacheXWorkers

class SingleCacheUseCase<T> constructor(
    private val worker: CacheXWorkers,
    private val storage: CacheXStorage
) : UseCase<Boolean, SingleParam<T>>() {
    override suspend fun run(params: SingleParam<T>): Either<Exception, Boolean> {
        return worker.cacheSingle(params.key, params.data, storage)
    }
}