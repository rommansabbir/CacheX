package com.rommansabbir.cachex

import android.content.Context
import android.content.SharedPreferences
import com.rommansabbir.cachex.list.CacheXListEncryptionImpl
import com.rommansabbir.cachex.single.CacheXSingleSingleEncryptionImpl
import com.rommansabbir.cachex.storage.CacheXStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CacheX(context: Context) {
    private var cache: CacheXStorage =
        CacheXStorage(context)

    private var cacheXSingleEncryptionImpl = CacheXSingleSingleEncryptionImpl()
    private var cacheXListEncryptionImpl = CacheXListEncryptionImpl()

    fun <T> doCache(data: T, key: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        CoroutineScope(scope).launch {
            if (encryptionKey.isNotEmpty()) {
                cacheXSingleEncryptionImpl.encryptToJSON(
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
            } else {
                notifyOnMain { onError.invoke(Exception(EMPTY_ENCRYPTION_KEY_ERROR)) }
            }
        }

    }

    fun <T> doCacheList(
        data: List<T>,
        key: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(scope).launch {
            if (encryptionKey.isNotEmpty()) {
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
            } else {
                notifyOnMain { onError.invoke(Exception(EMPTY_ENCRYPTION_KEY_ERROR)) }
            }
        }
    }

    fun <T> getCache(
        clazz: Class<T>,
        key: String,
        onSuccess: (T) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(scope).launch {
            if (encryptionKey.isNotEmpty()) {
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
            } else {
                notifyOnMain { onError.invoke(Exception(EMPTY_ENCRYPTION_KEY_ERROR)) }
            }
        }
    }

    fun <T> getCacheList(
        key: String,
        onSuccess: (MutableList<T>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(scope).launch {
            if (encryptionKey.isNotEmpty()) {
                cacheXListEncryptionImpl.decryptFromJSON<T>(
                    key,
                    cache,
                    {
                        notifyOnMain { onSuccess.invoke(it) }
                    },
                    {
                        notifyOnMain { onError.invoke(it) }
                    })
            } else {
                notifyOnMain { onError.invoke(Exception(EMPTY_ENCRYPTION_KEY_ERROR)) }
            }
        }
    }

    companion object {
        private var encryptionKey = ""
        private var scope: CoroutineContext = Dispatchers.IO + Job()

        fun initializeComponents(key: String): Boolean {
            this.encryptionKey = key
            return true
        }

        fun getKey(): String = encryptionKey

        const val EMPTY_ENCRYPTION_KEY_ERROR =
            "Encryption key empty, did you called initializeComponents() in application layer?"

        var hashSet: HashSet<String> = HashSet()

        fun unSubscribe(key: String) = hashSet.remove(key)

        var mXStorageListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key -> }
    }
}