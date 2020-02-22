package com.rommansabbir.cachex.list

import com.rommansabbir.cachex.CacheXCrypto
import com.rommansabbir.cachex.CacheXDataConverter
import com.rommansabbir.cachex.storage.CacheXStorage
import com.rommansabbir.cachex.storage.getEmitterException
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CacheXListEncryptionImpl : CacheXListEncryption {
    /**
     * Encrypt a given list of data in [T] format
     * Save encrypted data into local storage using the help of [CacheXStorage]
     * All works are done in background thread using help of RxJava2
     *
     * @param data, list data model of [T] typed
     * @param key, key that are going to be used to saved encrypted data in [CacheXStorage] in [String] format
     * @param xStorage, [CacheXStorage] reference to store encrypted data locally
     *
     * @return [Single<[String]>], it will return an emitter instance
     */
    override fun <T> encryptToJSON(
        data: List<T>,
        key: String,
        xStorage: CacheXStorage
    ): Single<String> {
        return Single.create { emitter: SingleEmitter<String> ->
            if (emitter.isDisposed) {
                emitter.onError(getEmitterException())
            } else {
                try {
                    val dataTemp = CacheXCrypto.encrypt(
                        CacheXDataConverter().toJson(data)
                    )
                    xStorage.doCache(dataTemp, key)
                    emitter.onSuccess("Success")
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Decrypt a list of data in [T] format
     * Get decrypted list of  data from local storage using the help of [CacheXStorage]
     * All works are done in background thread using help of RxJava2
     *
     * @param clazz, class type of [T]
     * @param key, key that were used to save encrypted data in [CacheXStorage] in [String] format
     * @param xStorage, [CacheXStorage] reference to get encrypted data from local storage
     *
     * @return [Single<[T]>], it will return an emitter instance
     */
    override fun <T> decryptFromJSON(
        clazz: Class<T>,
        key: String,
        xStorage: CacheXStorage
    ): Single<List<T>> {
        return Single.create { emitter: SingleEmitter<List<T>> ->
            if (emitter.isDisposed) {
                emitter.onError(getEmitterException())
            } else {
                val data = xStorage.getCache(key)
                if (data != null) {
                    try {
                        val decrypted = CacheXCrypto.decrypt(data)
                        val decryptedString = CacheXDataConverter().fromJSON<T>(decrypted)
                        val list = mutableListOf<T>()
                        for (item in decryptedString) {
                            list.add(CacheXDataConverter().fromJSON(item.toString(), clazz))
                        }
                        emitter.onSuccess(list)
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                } else {
                    emitter.onError(Exception("No data found"))
                }
            }

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}

interface CacheXListEncryption {
    fun <T> encryptToJSON(
        data: List<T>,
        key: String,
        xStorage: CacheXStorage
    ): Single<String>

    fun <T> decryptFromJSON(
        clazz: Class<T>,
        key: String,
        xStorage: CacheXStorage
    ): Single<List<T>>
}