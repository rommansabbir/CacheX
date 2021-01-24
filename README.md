[![Release](https://jitpack.io/v/jitpack/android-example.svg)](https://jitpack.io/#rommansabbir/CacheX)
# CacheX
A feasible caching library for Android.

## Features
* Real-time update
* Lightweight
* Secure
* Thread Safe

## How does it work?
Caching is just a simple key-value pair data saving procedure. CacheX follows the same approach. CacheX uses SharedPreference as storage for caching data. Since we really can't just save the original data because of security issues. CacheX uses AES encryption & decryption behind the scene when you are caching data or fetching data from the cache. Also, you can observer cached data in real-time.

## Documentation

### Installation

---
Step 1. Add the JitPack repository to your build file 

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency

```gradle
dependencies {
    implementation 'com.github.rommansabbir:CacheX:Tag'
}
```

---

### Version available

| Latest Releases
| ------------- |
| 2.0           |

---

# Usages
## Instantiate CacheX components in your app application class

**This step is important!!**

Define an encryption key to instantiate CacheX components.
This key will be used to encrypt or decrypt data using AES algorithm.

Initialize CacheX component by calling `CacheXCore.init()` pass contex, encryption key & app name for CacheX session.  

Don't forget to call at `CacheXCore.init()` from 
application layer otherwise, CacheX will not work properly and
will throw an exception.
            
````
    private val encryptionKey = "!x@4#w$%f^g&h*8(j)9b032ubfu8238!"

    override fun onCreate() {
        super.onCreate()
        CacheXCore.init(this, encryptionKey, getString(R.string.app_name))
    }
````

---

## How to access?

Call `CacheXCore.getInstance()` to get reference of `CacheX` and you can access all public methods.

---

## Available public methods
##### (Note: Most of the methods are `suspended` methods, so `suspened` methods must be called under a `CoroutineScope`)

* `fun <T> cacheSingle(key: String, data: T): Either<Exception, Boolean>` - To cache single data and return either `Exception`
 on error or `Boolean` on success.

* `fun <T> getCacheSingle(key: String, clazz: Class<T>): Either<Exception, T>` - To get single cached data from `CacheX`
based on the provided key and return `Exception` on error or `T` on success.

* `fun <T> cacheList(key: String, data: List<T>): Either<Exception, Boolean>` - To cache a list of `POJO` or `DataModel` 
and return either `Exception` on error or `Boolean` on success.

* `fun <T> getCacheList(key: String, clazz: Class<T>): Either<Exception, ArrayList<T>>` - To get list of cached data from `CacheX`
based on the provided key and return `Exception` on error or `ArrayList<T>` on success.

* `fun registerListener(callback: CacheXCallback, key: String)` - Register a new listener to get notified on data changes
based on the provided key.

* `fun registerListener(callback: CacheXCallback, key: ArrayList<String>)` - Register a list of listeners to get notified on data changes
based on the provided key list.

* `fun unregisterListener(key: String)` - Unregister a listener based on the provided key.

* `fun unregisterListener(key: ArrayList<String>)` - Unregister a list of listeners based on the provided key list.

* `fun clearListeners()` - To clear all listeners

* `fun clearCacheByKey(key: String): Exception?` - Clear a cache by the respective key which may `throw` `Exception`
if happpened.

* `fun clearAllCache(): Exception?` - Clear all cache which may `throw` `Exception` if happpened.

---

## How to use the callback to get notified on data changes?

* Implement the callback to `Activity` or `Fragment` or `ViewModel`.
````
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

    private fun getListFromCache(key: String) {...}

    private fun cacheLSingle(key: String, data: String) {...}
}
````

* Register a listener or list of listener from your `Activity` or `Fragment` `onResume()` method. In this example,
I have implemented the callback on my respective view model. 
````
    override fun onResume() {
        super.onResume()
        CacheXCore.getInstance().registerListener(viewModel, arrayListOf(LIST_KEY, SINGLE_KEY))
        //or 
        CacheXCore.getInstance().registerListener(viewModel, LIST_KEY)
    }
````

* To unregister a listener or list of listeners from your `Activity` or `Fragment` `onStop()` method.
````
    override fun onStop() {
        CacheXCore.getInstance().unregisterListener(arrayListOf(LIST_KEY, SINGLE_KEY))
        //or
        CacheXCore.getInstance().unregisterListener(LIST_KEY)
        super.onStop()
    }
````
---

##### Need more information regarding the solid implementation? - Check out the sample application.

---

### Contact me
[Portfolio](https://www.rommansabbir.com/) | [LinkedIn](https://www.linkedin.com/in/rommansabbir/) | [Twitter](https://www.twitter.com/itzrommansabbir/) | [Facebook](https://www.facebook.com/itzrommansabbir/)

### License

---
[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

````
Copyright (C) 2021 Romman Sabbir

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````


