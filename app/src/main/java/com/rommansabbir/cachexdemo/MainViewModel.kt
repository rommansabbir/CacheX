package com.rommansabbir.cachexdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rommansabbir.cachex.callback.CacheXCallback
import com.rommansabbir.cachex.core.CacheXCore
import com.rommansabbir.cachex.exceptions.CacheXListLimitException
import com.rommansabbir.cachex.params.GetListParam
import com.rommansabbir.cachex.params.GetSingleParam
import com.rommansabbir.cachex.params.ListParam
import com.rommansabbir.cachex.params.SingleParam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(), CacheXCallback {

    override fun onChanges(key: String) {
        when (key) {
            MainActivity.SINGLE_KEY -> {
                getSingleFromCache(key)
            }
            MainActivity.LIST_KEY -> {
                getListFromCache(key)
            }
        }

    }

    private var _onChangesList: MutableLiveData<ArrayList<UserAuth>> = MutableLiveData()

    internal val onChanges: LiveData<ArrayList<UserAuth>>
        get() = _onChangesList

    private var _onChangesSingle: MutableLiveData<String> = MutableLiveData()

    internal val onChangesSingle: LiveData<String>
        get() = _onChangesSingle

    val scope = CoroutineScope(Dispatchers.Default)

    init {
        getListFromCache(MainActivity.LIST_KEY)
        getSingleFromCache(MainActivity.SINGLE_KEY)
    }


    fun cacheList(key: String, list: ArrayList<UserAuth>, onSuccess: (String) -> Unit) {
        scope.launch {
            CacheXCore
                .getInstance()
                .cacheList<UserAuth>()
                .invoke(ListParam(key, list)) { useCase ->
                    useCase.either(
                        {
                            if (it is CacheXListLimitException) {
                                print(it.thread)
                                print(it.message)
                            } else {
                                println(it.message)
                            }
                        },
                        {
                            onSuccess.invoke("List Cached")
                        }
                    )
                }
        }
    }

    private fun getListFromCache(key: String) {
        scope.launch {
            CacheXCore
                .getInstance()
                .getCacheList<UserAuth>()
                .invoke(GetListParam(key, UserAuth::class.java)) {
                    it.either(::onGetListFromCacheError, ::onGetListFromCacheSuccess)
                }
        }
    }

    private fun onGetListFromCacheSuccess(data: ArrayList<UserAuth>) {
        _onChangesList.value = data
    }

    private fun onGetListFromCacheError(e: Exception) {
        println(e.message)
    }

    fun cacheLSingle(key: String, data: String) {
        CacheXCore
            .getInstance()
            .cacheSingle<String>().invoke(
                SingleParam(key, data)
            ) { useCase ->
                useCase.either(
                    {
                        println(it.message)
                    },
                    {
                        println("Cached")
                    }
                )
            }
    }

    private fun getSingleFromCache(key: String) {
        viewModelScope.launch {
            CacheXCore
                .getInstance()
                .getCacheSingle<String>().invoke(
                    GetSingleParam(key, String::class.java)
                ) {
                    it.either(::onGetSingleFromCacheError, ::onGetSingleFromCacheSuccess)
                }
        }
    }

    private fun onGetSingleFromCacheSuccess(data: String) {
        _onChangesSingle.value = data
    }

    private fun onGetSingleFromCacheError(e: Exception) {
        println(e.message)
    }
}