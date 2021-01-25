package com.rommansabbir.cachex.exceptions

import java.util.*

abstract class CacheXException constructor(override val message: String) : Exception(message) {
    var thread: String? = null
    var dateTime: Date? = null

    init {
        thread = Thread.currentThread().toString()
        dateTime = Calendar.getInstance().time
    }
}