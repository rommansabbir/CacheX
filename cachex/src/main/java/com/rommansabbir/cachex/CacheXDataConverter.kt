package com.rommansabbir.cachex

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CacheXDataConverter {

    fun <T> toJson(data: T): String = Gson().toJson(data)

    suspend fun <T> toJson(data: List<T>, onSuccess: suspend (String) -> Unit) {
        onSuccess.invoke(Gson().toJson(data, getTypeToken<T>()))
    }

    suspend fun <T> fromJSONSingle(value: String, clazz: Class<T>, onSuccess: suspend (T) -> Unit) {
        onSuccess.invoke(Gson().fromJson(value, clazz))
    }

    private fun <T> getTypeToken() = object : TypeToken<List<T>>() {}.type

    suspend fun <T> fromJSONList(data: String, onSuccess: suspend (MutableList<T>) -> Unit) {
        onSuccess.invoke(Gson().fromJson(data, getTypeToken<T>()))
    }
}

