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
        onSuccess: (T) -> Unit,
        onError: (Exception) -> Unit,
        lifecycleOwner: LifecycleOwner? = null,
        provideRealtimeUpdate: Boolean = true,
        coroutineContext: CoroutineContext? = null
    ) {
        getSingleCache(key, clazz, onSuccess, onError, coroutineContext)
        if (lifecycleOwner != null) {
            if (provideRealtimeUpdate) {
                listenerLiveData.observe(lifecycleOwner, Observer {
                    if (it == key) {
                        getSingleCache(key, clazz, onSuccess, onError, coroutineContext)
                    }
                })
            }
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
        clazz: Class<T>,
        key: String,
        onSuccess: (MutableList<T>) -> Unit,
        onError: (Exception) -> Unit,
        lifecycleOwner: LifecycleOwner? = null,
        provideRealtimeUpdate: Boolean = true,
        coroutineContext: CoroutineContext? = null
    ) {
        getCacheListMultiple(clazz, key, onSuccess, onError, coroutineContext)
        if (lifecycleOwner != null) {
            if (provideRealtimeUpdate) {
                listenerLiveData.observe(lifecycleOwner, Observer {
                    if (it == key) {
                        getCacheListMultiple(clazz, key, onSuccess, onError, coroutineContext)
                    }
                })
            }
        }
    }

    private fun <T> getCacheListMultiple(
        clazz: Class<T>,
        key: String,
        onSuccess: (MutableList<T>) -> Unit,
        onError: (Exception) -> Unit,
        coroutineContext: CoroutineContext?
    ) {
        if (encryptionKey.isNotEmpty()) {
            CoroutineScope(coroutineContext ?: scope).launch {
                executeCoroutine(
                    {
                        cacheXListEncryptionImpl.decryptFromJSON(
                            clazz,
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

    fun clearCacheByKey(
        key: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val error = cache.clearCacheByKey(key)
        if (error == null) {
            onSuccess.invoke("Cache cleared successfully")
        } else {
            onError.invoke(error)
        }
    }

    fun clearAllCache(
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val error = cache.clearAllCache()
        if (error == null) {
            onSuccess.invoke("Cache cleared successfully")
        } else {
            onError.invoke(error)
        }
    }

    companion object {
        private var encryptionKey = ""
        private var scope: CoroutineContext = Dispatchers.IO + Job()
        private lateinit var cache: CacheXStorage
        private lateinit var cacheX: CacheX

        fun initializeComponents(context: Context, key: String, appName: String): Boolean {
            this.encryptionKey = key
            this.cache = CacheXStorage(context, appName)
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