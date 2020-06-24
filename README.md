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

| Releases
| ------------- |
| 1.0           |
| 1.0.1         |
| 1.0.2         |

# Usages
### Instantiate CacheX components in your app application class

````
    // Define an encryption key to instantiate CacheX components.
    // This key will be used to encrypt or decrypt data using AES algorith
    
    private val encryptionKey = "!x@4#w$%f^g&h*8(j)9b032ubfu8238!"
    ...
    override fun onCreate() {
        ...
            // Initialize CacheX component by calling initializeComponents()  
            // pass context & encryption key for CacheX session.  
            
            // This step is important!!
            
            // Don't forget to call at initializeComponents() from 
            application layer otherwise, CacheX will not work properly and
            will throw an exception.
            
            CacheX.initializeComponents(this, encryptionKey)
    }
````
---

### Define key/keys & instantiate CacheX reference
```
            // Define keys for single & list item
            // Those keys will be used to cache data & fetch data
            private val key: String = "authKey"
            private val keySingle = "authKeySingle"

            // Instantiate CacheX reference by passing context as parameter
            
            var cacheX = CacheX.initCacheX()

            // Define model (single item) /models (list of item) of data 
            // Behind the scene CacheX use SharedPref as storage for caching
            // data which is simply a key value pair data saving procedure
           
            val model = SomeClass("romman", "testpass")
            val model1 = SomeClass("prottay", "testpass")
```
---

### Cache a single item or data model
````
            // When you want cache a single item like, data model, string, 
            numbers etc.
            
            // It better to use data model to wrapped multiple item into a 
            single object rather than explicitly saving single item.
            
            // You can pass your own coroutine context otherwise CacheX will 
            use it's own coroutine context
            
            cacheX.doCache(
                dataModel,
                keySingle,
                {
                	// Do your stuff on success
                },
                {
                	// Do your stuff on error
                })
````
---

### Fetch a single item from cache
````
		// When you want to get from cache a single item like, data model, 
        string, numbers
        
        // Now support, real-time updates on cache value updates, pass the 
        lifecycle owner reference of your activity or fragment to get update 
        on real-time for a specific key. You can also turn off the real-time 
        update feature by providing **provideRealtimeUpdate** to **false**
        
		// You can pass your own coroutine context otherwise CacheX will use 
        it's own coroutine context
              
              cacheX.getCache(
              SomeClass::class.java,
              keySingle,
              this,
              {
                  // Do your stuff on success
              },
              {
                  // Do your stuff on error
              }
          )
````
---

### Cache a list of items or data models
````
            // When you want cache a list of items like, data model, string, 
            numbers
            
            // You can pass your own coroutine context otherwise CacheX will 
            use it's own coroutine context
            
            cacheX.doCacheList(
                dataList,
                key,
                {
                	// Do your stuff on success
                },
                {
                    // Do your stuff on error
                })
````
---

### Fetch a list of  items from cache
````
            // When you want to get from cache a list of items like, data
            model, strings, numbers
            
			// Now support, real-time updates on cache value updates, pass
            the lifecycle owner reference of your activity or fragment to
            get update on real-time for a specific key. You can also turn
            off the real-time update feature by providing
            **provideRealtimeUpdate** to **false**
        
            // You can pass your own coroutine context otherwise CacheX
            will use it's own coroutine context
            
        	cacheX.getCacheList<SomeClass>(
            key,
            this,
            {
                // Do your stuff on success
            },
            {
                // Do your stuff on error
            }
        )
````
### Clear a cache by key
            
        	cacheX.clearCacheByKey(
            key,
            {
                // Do your stuff on success
            },
            {
                // Do your stuff on error
            }
        )

### Clear all cache
            
        	cacheX.clearAllCache(
            {
                // Do your stuff on success
            },
            {
                // Do your stuff on error
            }
        )

### Contact me
[Portfolio](https://www.rommansabbir.com/) | [LinkedIn](https://www.linkedin.com/in/rommansabbir/) | [Twitter](https://www.twitter.com/itzrommansabbir/) | [Facebook](https://www.facebook.com/itzrommansabbir/)

### License
---
[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

````
Copyright (C) 2020 Romman Sabbir

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


