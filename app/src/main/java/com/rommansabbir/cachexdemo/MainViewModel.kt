package com.rommansabbir.cachexdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rommansabbir.cachex.callback.CacheXCallback
import com.rommansabbir.cachex.core.CacheXCore
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


    fun cacheList(key: String, list: ArrayList<UserAuth>) {
        scope.launch {
            CacheXCore
                .getInstance()
                .cacheList(key, list)
        }
    }

    private fun getListFromCache(key: String) {
        scope.launch {
            CacheXCore
                .getInstance()
                .getCacheList(key, UserAuth::class.java)
                .either(::onGetListFromCacheError, ::onGetListFromCacheSuccess)
        }
    }

    private fun onGetListFromCacheSuccess(data: ArrayList<UserAuth>) {
        viewModelScope.launch {
            _onChangesList.value = data
        }
    }

    private fun onGetListFromCacheError(e: Exception) {
        println(e.message)
    }

    fun cacheLSingle(key: String, data: String) {
        viewModelScope.launch {
            CacheXCore
                .getInstance()
                .cacheSingle(key, data)
        }
    }

    private fun getSingleFromCache(key: String) {
        viewModelScope.launch {
            CacheXCore
                .getInstance()
                .getCacheSingle(key, String::class.java)
                .either(::onGetSingleFromCacheError, ::onGetSingleFromCacheSuccess)
        }
    }

    private fun onGetSingleFromCacheSuccess(data: String) {
        viewModelScope.launch {
            _onChangesSingle.value = data
        }
    }

    private fun onGetSingleFromCacheError(e: Exception) {
        println(e.message)
    }
}