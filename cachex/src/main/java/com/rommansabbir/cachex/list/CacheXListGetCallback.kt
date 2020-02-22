package com.rommansabbir.cachex.list

import com.rommansabbir.cachex.CacheXBaseCallback

interface CacheXListGetCallback: CacheXBaseCallback {
    fun<T> onSuccess(data :List<T>)
}