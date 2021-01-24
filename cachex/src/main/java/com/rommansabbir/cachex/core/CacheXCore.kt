package com.rommansabbir.cachex.core

import android.content.Context
import android.content.SharedPreferences
import com.rommansabbir.cachex.base.CacheX
import com.rommansabbir.cachex.base.CacheXImpl
import com.rommansabbir.cachex.exceptions.CacheXInitException
import com.rommansabbir.cachex.functional.Either
import com.rommansabbir.cachex.storage.CacheXStorage
import com.rommansabbir.cachex.storage.CacheXStorageImpl
import com.rommansabbir.cachex.worker.CacheXWorkers
import com.rommansabbir.cachex.worker.CacheXWorkersImpl

class CacheXCore private constructor(
    private val context: Context,
    private val appName: String
) {
    internal var cacheX: CacheX? = null
    private var cacheXStorage: CacheXStorage? = null
    private var cacheXWorkers: CacheXWorkers? = null

    private val listener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (cacheX != null) {
                if (cacheX!!.listenersList.containsKey(key)) {
                    val listener = cacheX!!.listenersList[key]
                    listener?.onChanges(key)
                }
            }
        }

    /**
     * Initialize all required classes to perform actions.
     * Register listener to notify on data changes.
     *
     * @return Either<Exception, Boolean>
     */
    fun init(): Either<Exception, Boolean> {
        return try {
            cacheXWorkers = CacheXWorkersImpl()
            cacheXStorage = CacheXStorageImpl(context, appName)
            this.cacheX =
                CacheXImpl(cacheXStorage!!, cacheXWorkers!!)
            cacheXStorage?.registerListener(listener)
            Either.Right(true)
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    companion object {
        private var core: CacheXCore? = null
        private var key: String = ""
        fun getKey() = key

        fun init(
            context: Context,
            encryptionKey: String,
            appName: String
        ): Either<Exception, Boolean> {
            return try {
                key = encryptionKey
                core = CacheXCore(context, appName)
                return core?.init()!!
            } catch (e: Exception) {
                Either.Left(e)
            }
        }

        fun getInstance(): CacheX {
            if (core == null) {
                throw CacheXInitException("CacheX not initialized properly. Make sure to initialize CacheX before accessing it's instance.")
            }
            return core?.cacheX!!
        }
    }
}