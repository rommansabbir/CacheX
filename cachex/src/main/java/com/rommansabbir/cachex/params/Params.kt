package com.rommansabbir.cachex.params

data class SingleParam<T>(val key: String, val data: T)

data class GetSingleParam<T>(val key: String, val clazz: Class<T>)

data class ListParam<T>(val key: String, val data: List<T>)

data class GetListParam<T>(val key: String, val clazz: Class<T>)