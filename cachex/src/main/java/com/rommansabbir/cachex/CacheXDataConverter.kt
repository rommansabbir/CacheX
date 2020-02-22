package com.rommansabbir.cachex

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CacheXDataConverter {
    /**
     * Convert a data model of [T] typed to string using the help of [Gson]
     *
     * @param data, data model of [T] typed
     */
    fun<T> toJson(data: T): String = Gson().toJson(data)


    /**
     * Convert a list of data model of [T] typed to string using the help of [Gson]
     *
     * @param data, list of data model of [T] typed
     *
     * @return [String], converted data will be returned in [String] format
     */
    fun<T> toJson(data: List<T>): String{
        return Gson().toJson(data, getTypeToken<T>())
    }

    /**
     * Get data model of [T] typed from a string using the help of [Gson]
     *
     * @param value, [String] data that need to be converted in [T]
     * @param clazz, data type of [T] wrapped into [Class<[T]>]
     *
     * @return [T], it will return the [T] typed data
     */
    fun<T> fromJSON(value: String, clazz : Class<T>): T = Gson().fromJson(value, clazz)

    /**
     * Get typed token from [T]
     *
     * @return [TypeToken], return type from [T]
     */
    private fun<T> getTypeToken() = object : TypeToken<List<T>>(){}.type

    /**
     * Get list of data model of [T] typed from a given [String] using the help of [Gson]
     *
     * @param data, data that need to be converted in list of [T]
     *
     * @return [List<[T]>], list of data model of [T] typed
     */
    fun <T> fromJSON(data: String): List<T> = Gson().fromJson(data, getTypeToken<T>())
}

