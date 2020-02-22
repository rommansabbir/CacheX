package com.rommansabbir.cachex.single

import com.rommansabbir.cachex.CacheXBaseCallback

interface CacheXSingleGetCallback: CacheXBaseCallback {
    fun<T> onSuccess(data : T)
}