package com.rommansabbir.cachex.single

import com.rommansabbir.cachex.CacheXCrypto
import com.rommansabbir.cachex.CacheXDataConverter
import com.rommansabbir.cachex.executeCoroutine
import com.rommansabbir.cachex.storage.CacheXStorage

class CacheXSingleSingleEncryptionImpl : CacheXSingleEncryption {

    override suspend fun <T> encryptToJSON(
        data: T,
        key: String,
        xStorage: CacheXStorage,
        onSuccess: suspend () -> Unit,
        onError: suspend (Exception) -> Unit
    ) {
        CacheXCrypto.encrypt(
            CacheXDataConverter().toJson(data)
        ) {
            xStorage.doCache(it, key)
            onSuccess.invoke()
        }
    }

    override suspend fun <T> decryptFromJSON(
        key: String,
        clazz: Class<T>,
        xStorage: CacheXStorage,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (Exception) -> Unit
    ) {
        val data = xStorage.getCache(key)
        if (data != null) {
            CacheXCrypto.decrypt(data) { it ->
                CacheXDataConverter().fromJSONSingle(it, clazz) {
                    onSuccess.invoke(it)
                }
            }
        } else {
            onError.invoke(Exception("No data found"))
        }
    }
}

interface CacheXSingleEncryption {
    suspend fun <T> encryptToJSON(
        data: T,
        key: String,
        xStorage: CacheXStorage,
        onSuccess: suspend () -> Unit,
        onError: suspend (Exception) -> Unit
    )

    suspend fun <T> decryptFromJSON(
        key: String,
        clazz: Class<T>,
        xStorage: CacheXStorage,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (Exception) -> Unit
    )
}



