package com.rommansabbir.cachex.callback

interface CacheXCallback {
    /**
     * Notify listener when data changes occur based on the key.
     *
     * @param key Key of the data which was changed or updated or created
     */
    fun onChanges(key: String)
}