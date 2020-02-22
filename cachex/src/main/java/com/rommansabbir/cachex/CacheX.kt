package com.rommansabbir.cachex

import android.content.Context
import com.rommansabbir.cachex.list.CacheXListEncryptionImpl
import com.rommansabbir.cachex.list.CacheXListGetCallback
import com.rommansabbir.cachex.single.CacheXSingleSingleEncryptionImpl
import com.rommansabbir.cachex.single.CacheXSingleGetCallback
import com.rommansabbir.cachex.storage.CacheXStorage
import io.reactivex.disposables.CompositeDisposable
import java.lang.RuntimeException

class CacheX(private val context: Context) {
    private var cache : CacheXStorage =
        CacheXStorage(context)
    private var cacheXSingleEncryptionImpl = CacheXSingleSingleEncryptionImpl()
    private var cacheXListEncryptionImpl = CacheXListEncryptionImpl()

    /**
     * Cache a single data model (generic) of type [T] with AES encryption
     * Check [encryptionKey] key status, if empty throw [RuntimeException]
     *
     * @param data, [T] instance generic data model
     * @param key, An specific key of type [String] to save data as key value pair
     * @param callback, [CacheXSaveCallback] to provide caching status, either it succeed or not with error
     */
    fun<T> doCache(data: T, key:String, callback: CacheXSaveCallback){
        if(encryptionKey.isNotEmpty()){
            compositeDisposable.add(
                cacheXSingleEncryptionImpl.encryptToJSON(data, key, cache).subscribe(
                    {
                        callback.onSuccess()
                    },
                    {
                        callback.onError(it)
                    }
                )
            )
        }
        else{
            throw RuntimeException(EMPTY_ENCRYPTION_KEY_ERROR)
        }
    }

    /**
     * Cache a list data model (generic) of type [T] with AES encryption
     * Check [encryptionKey] key status, if empty throw [RuntimeException]
     *
     * @param data, [List<[T]>] instance generic data model
     * @param key, An specific key of type [String] to save data as key value pair
     * @param callback, [CacheXSaveCallback] to provide caching status, either it succeed or not with error
     */
    fun<T> doCache(data: List<T>, key:String, callback: CacheXSaveCallback){
        if(encryptionKey.isNotEmpty()){
            compositeDisposable.add(
                cacheXListEncryptionImpl.encryptToJSON(data, key, cache).subscribe(
                    {
                        callback.onSuccess()
                    },
                    {
                        callback.onError(it)
                    }
                )
            )
        }
        else{
            throw RuntimeException(EMPTY_ENCRYPTION_KEY_ERROR)
        }
    }

    /**
     * Get [T] typed data model from cache using AES Decryption's
     * Check [encryptionKey] key status, if empty throw [RuntimeException]
     *
     * @param clazz, data type [T]
     * @param key, the key that has been used to cache data in key value pair
     * @param callback, [CacheXSingleGetCallback] to provide caching status, either it succeed with data or not with error
     */
    fun<T> getCache(clazz : Class<T>, key: String, callback: CacheXSingleGetCallback){
        if(encryptionKey.isNotEmpty()){
            compositeDisposable.add(
                cacheXSingleEncryptionImpl.decryptFromJSON(clazz,key, cache).subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        callback.onError(it)
                    }
                )
            )
        }
        else{
            throw RuntimeException(EMPTY_ENCRYPTION_KEY_ERROR)
        }
    }

    /**
     * Get list of [T] typed data model from cache using AES Decryption's
     * Check [encryptionKey] key status, if empty throw [RuntimeException]
     *
     * @param clazz, data type [List<[T]>]
     * @param key, the key that has been used to cache data in key value pair
     * @param callback, [CacheXListGetCallback] to provide caching status, either it succeed with data or not with error
     */
    fun<T> getCache(clazz: Class<T>, key: String, callback: CacheXListGetCallback){
        if(encryptionKey.isNotEmpty()){
            compositeDisposable.add(
                cacheXListEncryptionImpl.decryptFromJSON(clazz, key, cache).subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        callback.onError(it)
                    }
                )
            )
        }
        else{
            throw RuntimeException(EMPTY_ENCRYPTION_KEY_ERROR)
        }
    }

    companion object{
        /**
         * To perform caching action in background using RxJava2.
         * This [CompositeDisposable] instance reference will be used all over [CacheX]
         */
        lateinit var compositeDisposable: CompositeDisposable

        /**
         * AES Encryption Key
         */
        private var encryptionKey = ""

        /**
         * To perform all [CacheX] actions, need to instantiate [CompositeDisposable] & set encryption's key for caching
         *
         * @param key, pass a key in [String] format
         * @return [Boolean], it will return true
         */
        fun initializeComponents(key: String):Boolean{
            this.encryptionKey = key
            this.compositeDisposable = CompositeDisposable()
            return true
        }

        /**
         * Get instantiated encryption's key
         */
        fun getKey():String = encryptionKey

        const val EMPTY_ENCRYPTION_KEY_ERROR= "Encryption key empty, did you called initializeComponents() in application layer?"
    }
}