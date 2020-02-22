package com.rommansabbir.cachexdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rommansabbir.cachex.CacheX
import com.rommansabbir.cachex.CacheXSaveCallback
import com.rommansabbir.cachex.list.CacheXListGetCallback
import com.rommansabbir.cachex.single.CacheXSingleGetCallback
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    // Define keys for single & list item
    private val key: String = "authKey"
    private val keySingle = "authKeySingle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiate CacheX by passing context as parameter
        var cacheX = CacheX(this)

        // Define model (single item) /models (list of item) of data that you want to cache with AES encryption
        // Behind the scene CacheX use SharedPref as storage for caching
        // data which is simply a key value pair data saving procedure
        val model = UserAuth("romman", "testpass")
        val model1 = UserAuth("prottay", "testpass")

        val dataList = mutableListOf(model, model1)

        btnEncryptSingle.setOnClickListener {

            // When you want cache a single item like, data model, string, numbers
            // It better to use data model to wrapped multiple item into a single object
            // rather than explicitly saving single items
            cacheX.doCache(model, keySingle, object : CacheXSaveCallback {
                override fun onSuccess() {
                    showMessage(model.username)
                }

                override fun onError(e: Throwable) {
                    showMessage(e.message.toString())
                }

            })
        }

        btnDecryptSingle.setOnClickListener {
            // When you want to get from cache a single item like, data model, string, numbers
            cacheX.getCache(UserAuth::class.java, keySingle, object : CacheXSingleGetCallback {
                override fun <T> onSuccess(data: T) {
                    //Cast data to your specific data type since it return generic response
                    showMessage((data as UserAuth).username)
                }

                override fun onError(e: Throwable) {
                    showMessage(e.message.toString())
                }
            })
        }

        btnEncrypt.setOnClickListener {
            // When you want cache a list of items like, data model, string, numbers
            // It better to use data model to wrapped multiple item into a single object
            // rather than explicitly saving single items
            cacheX.doCache(dataList, key, object : CacheXSaveCallback {
                override fun onError(e: Throwable) {
                    showMessage(e.message.toString())
                }

                override fun onSuccess() {
                    for (item in dataList) {
                        showMessage(item.username)
                    }
                }
            })
        }

        btnDecrypt.setOnClickListener {
            // When you want to get from cache a list of items like, data model, string, numbers
            cacheX.getCache(UserAuth::class.java, key, object : CacheXListGetCallback {
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
        }

    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}