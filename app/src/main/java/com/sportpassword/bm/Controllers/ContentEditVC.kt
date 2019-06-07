package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.activity_content_edit_vc.*

class ContentEditVC : BaseActivity() {

    var title: String = "選擇"
    var key: String? = null
    var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_edit_vc)

        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")
        }
        setMyTitle(title)


        if (intent.hasExtra("content")) {
            content = intent.getStringExtra("content")
            edittext.setText(content!!)
        }

        if (intent.hasExtra("key")) {
            key = intent.getStringExtra("key")
        }
        if (key == null) {
            //alertError()
        }
    }

    fun clear(view: View) {
        edittext.setText("")
    }

    fun submit(view: View) {

        val content = edittext.text.toString()
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
