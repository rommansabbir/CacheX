package com.rommansabbir.cachex

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.rommansabbir.cachex.list.CacheXListEncryptionImpl
import com.rommansabbir.cachex.single.CacheXSingleSingleEncryptionImpl
import com.rommansabbir.cachex.storage.CacheXStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CacheX private constructor() {
    private lateinit var listenerLiveData: CacheXLiveEvent<String>
    private var cacheXSingleEncryptionImpl = CacheXSingleSingleEncryptionImpl()
    private var cacheXListEncryptionImpl = CacheXListEncryptionImpl()

    fun <T> doCache(
        data: T,
        key: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
        coroutineContext: CoroutineContext? = null
    ) {
        if (encryptionKey.isNotEmpty()) {
            CoroutineScope(coroutineContext ?: scope).launch {
                executeCoroutine(
                    {
                        cacheXSingleEncryptionImpl.encryptToJSON(
                            data,
                            key,
                            cache,
                            {
                                notifyOnMain { onSuccess.invoke() }
                            },
                            {
                                notifyOnMain { onError.invoke(it) }
                            })
                    },
                    {
                        notifyOnMain { onError.invoke(it) }
                    }
                )
            }

        } else {
            onError.invoke(Exception(EMPTY_ENCRYPTION_KEY_ERROR))
        }


    }

    fun <T> doCacheList(
        data: List<T>,
        key: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
        coroutineContext: CoroutineContext? = null
    ) {
        if (encryptionKey.isNotEmpty()) {
            CoroutineScope(coroutineContext ?: scope).launch {
                executeCoroutine(
                    {
                        cacheXListEncryptionImpl.encryptToJSON(
                            data,
                            key,
                            cache,
                            {
                                notifyOnMain {
                                    onSuccess.invoke()
                                }
                            },
                            {
                                notifyOnMain {
                                    onError.invoke(it)
                                }
                            })
                    },
                    {
                        notifyOnMain { onError.invoke(it) }
                    }
                )

            }
        } else {
            onError.invoke(Exception(EMPTY_ENCRYPTION_KEY_ERROR))
        }
    }

    fun <T> getCache(
        clazz: Class<T>,
        key: String,
        lifecycleOwner: LifecycleOwner,
        onSuccess: (T) -> Unit,
        onError: (Exception) -> Unit,
        provideRealtimeUpdate: Boolean = true,
        coroutineContext: CoroutineContext? = null
    ) {
        getSingleCache(key, clazz, onSuccess, onError, coroutineContext)
        if (provideRealtimeUpdate) {
            listenerLiveData.observe(lifecycleOwner, Observer {
                if (it == key) {
                    getSingleCache(key, clazz, onSuccess, onError, coroutineContext)
                }
            })
        }
    }

    private fun <T> getSingleCache(
        key: String,
        clazz: Class<T>,
        onSuccess: (T) -> Unit,
        onError: (Exception) -> Unit,
        coroutineContext: CoroutineContext?
    ) {
        if (encryptionKey.isNotEmpty()) {
            CoroutineScope(coroutineContext ?: scope).launch {
                executeCoroutine(
                    {
                        cacheXSingleEncryptionImpl.decryptFromJSON(
                            key,
                            clazz,
                            cache,
                            {
                                notifyOnMain {
                                    onSuccess.invoke(it)
                                }
                            },
                            {
                                notifyOnMain {
                                    onError.invoke(it)
                                }
                            })
                    },
                    {
                        notifyOnMain {
                            onError.invoke(it)
                        }
                    })

            }
        } else {
            onError.invoke(Exception(EMPTY_ENCRYPTION_KEY_ERROR))
        }
    }

    fun <T> getCacheList(
        key: String,
        lifecycleOwner: LifecycleOwner,
        onSuccess: (MutableList<T>) -> Unit,
        onError: (Exception) -> Unit,
        provideRealtimeUpdate: Boolean = true,
        coroutineContext: CoroutineContext? = null
    ) {
        getCacheListMultiple(key, onSuccess, onError, coroutineContext)
        if (provideRealtimeUpdate) {
            listenerLiveData.observe(lifecycleOwner, Observer {
                if (it == key) {
                    getCacheListMultiple(key, onSuccess, onError, coroutineContext)
                }
            })
        }
    }

    private fun <T> getCacheListMultiple(
        key: String,
        onSuccess: (MutableList<T>) -> Unit,
        onError: (Exception) -> Unit,
        coroutineContext: CoroutineContext?
    ) {
        if (encryptionKey.isNotEmpty()) {
            CoroutineScope(coroutineContext ?: scope).launch {
                executeCoroutine(
                    {
                        cacheXListEncryptionImpl.decryptFromJSON<T>(
                            key,
                            cache,
                            {
                                notifyOnMain { onSuccess.invoke(it) }
                            },
                            {
                                notifyOnMain { onError.invoke(it) }
                            })
                    },
                    {
                        notifyOnMain { onError.invoke(it) }
                    }
                )

            }
        } else {
            onError.invoke(Exception(EMPTY_ENCRYPTION_KEY_ERROR))
        }
    }

    companion object {
        private var encryptionKey = ""
        private var scope: CoroutineContext = Dispatchers.IO + Job()
        private lateinit var cache: CacheXStorage
        private lateinit var cacheX: CacheX

        fun initializeComponents(context: Context, key: String): Boolean {
            this.encryptionKey = key
            this.cache = CacheXStorage(context)
            this.cache.registerListener(realtimeListener)
            return true
        }

        fun initCacheX(): CacheX {
            this.cacheX = CacheX()
            this.cacheX.listenerLiveData = CacheXLiveEvent()
            return this.cacheX
        }

        fun getKey(): String = encryptionKey

        const val EMPTY_ENCRYPTION_KEY_ERROR =
            "Encryption key empty, did you called initializeComponents() in application layer?"

        private var realtimeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                when (this.cacheX.listenerLiveData.value) {
                    null -> {
                        this.cacheX.listenerLiveData.setValue(key)
                    }
                    else -> {
                        this.cacheX.listenerLiveData.setValue(key)
                    }
                }
            }
    }
}