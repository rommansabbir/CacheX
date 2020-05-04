package com.rommansabbir.cachex.list

import com.rommansabbir.cachex.CacheXCrypto
import com.rommansabbir.cachex.CacheXDataConverter
import com.rommansabbir.cachex.storage.CacheXStorage


class CacheXListEncryptionImpl : CacheXListEncryption {

    override suspend fun <T> encryptToJSON(
        data: List<T>,
        key: String,
        xStorage: CacheXStorage,
        onSuccess: suspend () -> Unit,
        onError: suspend (Exception) -> Unit
    ) {
        CacheXDataConverter().toJson(
            data
        ) { it ->
            CacheXCrypto.encrypt(it) {
                xStorage.doCache(it, key)
                onSuccess.invoke()
            }

        }
    }

    override suspend fun <T> decryptFromJSON(
        key: String,
        xStorage: CacheXStorage,
        onSuccess: suspend (MutableList<T>) -> Unit,
        onError: suspend (Exception) -> Unit
    ) {
        val data = xStorage.getCache(key)
        if (data != null) {
            CacheXCrypto.decrypt(data) { it ->
                CacheXDataConverter().fromJSONList<T>(it) {
                    onSuccess.invoke(it)
                }
            }

        } else {
            onError.invoke(Exception("No data found"))
        }
    }

}

interface CacheXListEncryption {
    suspend fun <T> encryptToJSON(
        data: List<T>,
        key: String,
        xStorage: CacheXStorage,
        onSuccess: suspend () -> Unit,
        onError: suspend (Exception) -> Unit
    )

    suspend fun <T> decryptFromJSON(
        key: String,
        xStorage: CacheXStorage,
        onSuccess: suspend (MutableList<T>) -> Unit,
        onError: suspend (Exception) -> Unit
    )
}