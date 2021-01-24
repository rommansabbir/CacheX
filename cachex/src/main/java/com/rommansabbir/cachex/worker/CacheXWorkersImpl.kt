package com.rommansabbir.cachex.worker

import com.google.gson.Gson
import com.rommansabbir.cachex.converter.CacheXDataConverter
import com.rommansabbir.cachex.exceptions.CacheXNoDataException
import com.rommansabbir.cachex.functional.Either
import com.rommansabbir.cachex.security.CacheXEncryptionTool
import com.rommansabbir.cachex.storage.CacheXStorage


class CacheXWorkersImpl : CacheXWorkers {

    override suspend fun <T> cacheList(
        key: String,
        data: List<T>,
        xCacheXStorage: CacheXStorage
    ): Either<Exception, Boolean> {
        return try {
            xCacheXStorage.doCache(
                CacheXEncryptionTool.encrypt(CacheXDataConverter().toJson(data)),
                key
            )
            Either.Right(true)
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    override suspend fun <T> getCacheList(
        key: String,
        clazz: Class<T>,
        xCacheXStorage: CacheXStorage
    ): Either<Exception, ArrayList<T>> {
        return try {
            val data = xCacheXStorage.getCache(key)
            if (data != null) {
                val result =
                    CacheXDataConverter().fromJSONList<T>(CacheXEncryptionTool.decrypt(data))
                if (clazz.canonicalName == String::class.java.canonicalName) {
                    result.toArray()
                    return Either.Right(result)
                }
                if (clazz.canonicalName == Number::class.java.canonicalName) {
                    result.toArray()
                    return Either.Right(result)
                }
                if (clazz.canonicalName == Int::class.java.canonicalName) {
                    result.toArray()
                    return Either.Right(result)
                }
                if (clazz.canonicalName == Double::class.java.canonicalName) {
                    result.toArray()
                    return Either.Right(result)
                }
                if (clazz.canonicalName == Float::class.java.canonicalName) {
                    result.toArray()
                    return Either.Right(result)
                }
                if (clazz.canonicalName == Long::class.java.canonicalName) {
                    result.toArray()
                    return Either.Right(result)
                }
                if (clazz.canonicalName == Boolean::class.java.canonicalName) {
                    result.toArray()
                    return Either.Right(result)
                }
                val tempDataList = arrayListOf<T>()
                result.forEach {
                    tempDataList.add(Gson().fromJson(Gson().toJsonTree(it).asJsonObject, clazz))
                }
                Either.Right(tempDataList)
            } else {
                Either.Left(CacheXNoDataException("No data found"))
            }
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    override suspend fun <T> cacheSingle(
        key: String,
        data: T,
        xStorage: CacheXStorage
    ): Either<Exception, Boolean> {
        return try {
            xStorage.doCache(CacheXEncryptionTool.encrypt(CacheXDataConverter().toJson(data)), key)
            Either.Right(true)
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    override suspend fun <T> getCacheSingle(
        key: String,
        clazz: Class<T>,
        xCacheXStorage: CacheXStorage
    ): Either<Exception, T> {
        return try {
            val data = xCacheXStorage.getCache(key)
            return if (data != null) {
                val result =
                    CacheXDataConverter().fromJSONSingle(CacheXEncryptionTool.decrypt(data), clazz)
                Either.Right(result)
            } else {
                Either.Left(CacheXNoDataException("No data found"))
            }
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

}

