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
        executeCoroutine(
            {
                CacheXCrypto.encrypt(
                    CacheXDataConverter().toJson(data)
                ) {
                    xStorage.doCache(it, key)
                    onSuccess.invoke()
                }

            },
            {
                onError.invoke(it)
            }
        )
    }

    override suspend fun <T> decryptFromJSON(
        key: String,
        clazz: Class<T>,
        xStorage: CacheXStorage,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (Exception) -> Unit
    ) {
        executeCoroutine(
            {
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
            },
            {
                onError.invoke(it)
            }
        )

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



