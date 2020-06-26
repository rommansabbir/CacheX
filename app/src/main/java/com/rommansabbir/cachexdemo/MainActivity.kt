package com.rommansabbir.cachexdemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rommansabbir.cachex.CacheX
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.cancel
import java.util.*
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() {
    private val key: String = "authKey"
    private val keySingle = "authKeySingle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cacheX = CacheX.initCacheX()

        val model = UserAuth("romman", Calendar.getInstance().time)
        val model1 = UserAuth("prottay", Calendar.getInstance().time)

        val dataList = mutableListOf(model, model1)

        var tempScope: CoroutineContext? = null
        btnEncryptSingle.setOnClickListener {
            cacheX.doCache(
                "Hello Test",
                keySingle,
                {

                },
                {
                    showMessage(it.message.toString())
                })

        }
        btnDecryptSingle.setOnClickListener {
            tempScope?.cancel(CancellationException("Canceled by user"))
        }

        cacheX.getCache(
            String::class.java,
            keySingle,
            this,
            {
                showMessage(it)
            },
            {
                showMessage(it.message.toString())
            }
        )


        btnEncrypt.setOnClickListener {
            cacheX.doCacheList(
                dataList,
                key,
                {
                },
                {
                    showMessage(it.message.toString())
                })
        }

        btnDecrypt.setOnClickListener {
            cacheX.getCacheList(
                UserAuth::class.java,
                key,
                this,
                {
                    showMessage(it.size.toString())
                },
                {
                    showMessage(it.message.toString())
                }
            )
        }


    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun printLog(message: String) {
        Log.d("CacheX", message)
    }
}