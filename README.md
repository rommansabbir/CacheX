[![Release](https://jitpack.io/v/jitpack/android-example.svg)](https://jitpack.io/#rommansabbir/CacheX/1.0-beta)
# CacheX
A feasible caching library for Android.

## How does it work?
Caching is just a simple key-value pair data saving procedure. CacheX follows the same approach. CacheX uses SharedPreference as storage for caching data. Since we really can't just save the original data because of security issues. CacheX uses AES encryption & decryption behind the scene when you are caching data or fetching data from the cache.

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
| 1.0-beta      |

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
            // & pass encryption key for CacheX session.  
            // This step is important!!
            // Don't forget to call at initializeComponents() from application layer  
            // otherwise, CacheX will throw a Runtime Exception
            CacheX.initializeComponents(encryptionKey)
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
            var cacheX = CacheX(this)

            // Define model (single item) /models (list of item) of data that you want to cache with AES encryption
            // Behind the scene CacheX use SharedPref as storage for caching
            // data which is simply a key value pair data saving procedure
            val model = SomeClass("romman", "testpass")
            val model1 = SomeClass("prottay", "testpass")
```
---

### Cache a single item or data model
````
            // When you want cache a single item like, data model, string, numbers
            // It better to use data model to wrapped multiple item into a single object
            // rather than explicitly saving single items
            cacheX.doCache(model, keySingle, object : CacheXSaveCallback {
                override fun onSuccess() {
                    //TODO
                }

                override fun onError(e: Throwable) {
                    //TODO
                }

            })
````
---

### Fetch a single item from cache
````
            // When you want to get from cache a single item like, data model, string, numbers
            cacheX.getCache(SomeClass::class.java, keySingle, object : CacheXSingleGetCallback {
                  override fun <T> onSuccess(data: T) {
                        //Cast data to your specific data type since it return generic response
                        //TODO
                        showMessage((data as UserAuth).username)
                  }

                  override fun onError(e: Throwable) {
                          //TODO
                  }
             })
````
---

### Cache a list of items or data models
````
            // When you want cache a list of items like, data model, string, numbers
            // It better to use data model to wrapped multiple item into a single object
            // rather than explicitly saving single items
            cacheX.doCache(dataList, key, object : CacheXSaveCallback {
                override fun onError(e: Throwable) {
                    //TODO
                }

                override fun onSuccess() {
                    for (item in dataList) {
                        //Cast data to your specific data type since it return generic response
                        //TODO
                    }
                }
            })
````
---

### Fetch a list of  items from cache
````
            // When you want to get from cache a list of items like, data model, strings, numbers
            cacheX.getCache(SomeClass::class.java, key, object : CacheXListGetCallback {
                override fun onError(e: Throwable) {
                    showMessage(e.message.toString())
                }

                override fun <T> onSuccess(data: List<T>) {
                    for (item in data) {
                        //Cast data to your specific data type since it return generic response
                        showMessage((item as UserAuth).username)
                    }
                }

            })
````

### Contact me
[Portfolio](https://www.rommansabbir.com/) | [LinkedIn](https://www.linkedin.com/in/rommansabbir/) | [Twitter](https://www.twitter.com/itzrommansabbir/) | [Facebook](https://www.facebook.com/itzrommansabbir/)

