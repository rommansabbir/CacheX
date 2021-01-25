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
| 2.1.0         |

---

## What's new in this version?
* Implementation of UseCase which interact between the app layer & library layer
* CacheX works asynchronously
* Performance Improved

## What is UseCase & what its implementation?
* Simply UseCase in an interactor between two layer to communicate with each others (If you know you about Android Clean Architecture, you must be familiar with UseCase. It is one of the core component of ACA)
* UseCase take input from the respective thread and execute the whole operatin under a Background Thread & return the data on Main Thread

### What about the implementation or the work flow?
* Initialize CacheX properly
* Get an instance of CacheX (Instance is singleton)
* Call proper method to cahce data or get data from cache
* Method return a respective use case
* Provide the proper param to the use case & use case will return either expected data or an exception

Simple, huh? (Feel free to give any suggestion, I would love to get suggestion for further improvement)

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

## What's the purpose of the UseCase?

## Available public methods

* `fun <T> cacheSingle(): SingleCacheUseCase<T>` - To cache single data which return an instance of `SingleCacheUseCase`

* `fun <T : Any> getCacheSingle(): GetSingleCacheUseCase<T>` - To get single data from cache which return an instance of `GetSingleCacheUseCase`

* `fun <T> cacheList(): ListCacheUseCase<T>` - To cache list of data which return an instance of `ListCacheUseCase`

* `fun <T : Any> getCacheList(): GetListCacheUseCase<T>` - To get list of cached data from cache which return an instance of `GetListCacheUseCase`

* `fun registerListener(callback: CacheXCallback, key: String)` - To register listener respective to a `Key` & notify the listener through the callback on data changes

* `fun registerListener(callback: CacheXCallback, key: ArrayList<String>)` - - To register list of listeners respective to a `Key List` & notify the listener through the callback on data changes

* `fun unregisterListener(key: String)` - Unregister any listener according to the respective key

* `fun unregisterListener(key: ArrayList<String>)` - Unregister list of listeners according to the respective key list

* `fun clearListeners()` - Clear all listeners

* `fun clearCacheByKey(key: String)` - Clear any cache by it's key

* `fun clearAllCache()` - Clear all cache

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


