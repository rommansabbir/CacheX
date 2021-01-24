package com.rommansabbir.cachexdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AnotherActivity : AppCompatActivity() {
    var repeater = 0
    private val realtimeCache = "RealtimeTesting"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another)
        repeater = intent.getIntExtra("counter", 0)
//        val cacheX = CacheX.getInstance()
//        doTheMagic(cacheX)
//        cacheX.getCache(String::class.java, realtimeCache, {
//            showMessage(it)
//        }, {
//            showMessage(it.message.toString())
//        }, this)
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

//    private fun doTheMagic(cacheX: CacheX) {
//        CoroutineScope(Dispatchers.IO).launch {
//            delay(3000)
//            repeater += 1
//            cacheX.doCache("Cached $repeater times", realtimeCache, {
//                doTheMagic(cacheX)
//            }, {
//
//            })
//        }
//    }
}