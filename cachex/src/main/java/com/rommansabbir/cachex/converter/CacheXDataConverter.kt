package com.rommansabbir.cachex.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CacheXDataConverter {

    /**
     * Convert generic data to [String] using [Gson].
     *
     * @param data Generic data ([T])
     *
     * @return [String]
     */
    fun <T> toJson(data: T): String = Gson().toJson(data)

    /**
     * Convert list of generic data to [String] using [Gson].
     *
     * @param data List of Generic data ([T])
     *
     * @return [String]
     */
    fun <T> toJson(data: List<T>): String {
        return Gson().toJson(data, getTypeToken<T>())
    }

    /**
     * Convert [value] to specific data type ([T]) using [Gson]
     *
     * @param value Value that need to be converted to [T]
     * @param clazz Data type of the generic
     *
     * @return [T]
     */
    fun <T> fromJSONSingle(value: String, clazz: Class<T>): T {
        return Gson().fromJson(value, clazz)
    }

    /**
     * Get type token
     */
    private fun <T> getTypeToken() = object : TypeToken<ArrayList<T>>() {}.type

    /**
     * Convert [data] to specific data type (List) ([T]) using [Gson]
     *
     * @param data Value that need to be converted to list of [T]
     *
     * @return [ArrayList<T>]
     */
    fun <T> fromJSONList(
        data: String
    ): ArrayList<T> {
        return Gson().fromJson(data, getTypeToken<T>())
    }
}

