package com.rommansabbir.cachexdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.rommansabbir.cachex.core.CacheXCore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    companion object {
        val LIST_KEY = "ListKey"
        val SINGLE_KEY = "SingleKey"
    }

    private var repeater = 0


    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.onChanges.observe(this) { it ->
            it.let { arrayList ->
                var dataFound = "List Update"
                arrayList.forEach {
                    dataFound += "${it}, "
                }
                findViewById<TextView>(R.id.tv_list_update).text = dataFound
            }
        }
        viewModel.onChangesSingle.observe(this) {
            findViewById<TextView>(R.id.tv_single_update).text = "Single Update: $it"
        }

        findViewById<Button>(R.id.btnEncrypt).setOnClickListener {
            viewModel.scope.launch {
                val list = arrayListOf<UserAuth>()
                for (i in 0..Random.nextInt(0, 100)) {
                    list.add(UserAuth(i.toString(), i.toString()))
                }
                viewModel.cacheList(LIST_KEY, list)
            }
        }

        findViewById<Button>(R.id.btnEncryptSingle).setOnClickListener {
            viewModel.cacheLSingle(SINGLE_KEY, java.util.UUID.randomUUID().toString())
        }

        btn_next.setOnClickListener {
            val intent = Intent(this, AnotherActivity::class.java)
            intent.putExtra("counter", repeater)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        CacheXCore.getInstance().registerListener(viewModel, arrayListOf(LIST_KEY, SINGLE_KEY))
        //or
        CacheXCore.getInstance().registerListener(viewModel, LIST_KEY)
    }

    override fun onStop() {
        CacheXCore.getInstance().unregisterListener(arrayListOf(LIST_KEY, SINGLE_KEY))
        //or
        CacheXCore.getInstance().unregisterListener(LIST_KEY)
        super.onStop()
    }

    private fun showMessage(msg: String) {
        runOnUiThread {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun printLog(message: String) {
        Log.d("CacheX", message)
    }
}