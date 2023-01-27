package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.sportpassword.bm.databinding.ActivityContentEditVcBinding

class ContentEditVC : BaseActivity() {

    private lateinit var binding: ActivityContentEditVcBinding
    private lateinit var view: ViewGroup

    var title: String = "選擇"
    var key: String? = null
    var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContentEditVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")!!
        }
        setMyTitle(title)


        if (intent.hasExtra("content")) {
            content = intent.getStringExtra("content")
            binding.edittext.setText(content!!)
        }

        if (intent.hasExtra("key")) {
            key = intent.getStringExtra("key")
        }
        if (key == null) {
            //alertError()
        }

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun clear(view: View) {
        binding.edittext.setText("")
    }

    fun submit(view: View) {

        val content = binding.edittext.text.toString()
        val intent = Intent()
        intent.putExtra("key", key)
        intent.putExtra("content", content)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun cancel(view: View) {
        finish()
    }
}
