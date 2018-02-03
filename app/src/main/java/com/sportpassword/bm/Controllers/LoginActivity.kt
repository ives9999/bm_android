package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toolbar
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.app_bar_main.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //setSupportActionBar(toolbar)
        //supportActionBar!!.setTitle(resources.getText(R.string.login))

        //supportActionBar!!.setDisplayShowTitleEnabled(false)
        //setTitle(resources.getText(R.string.login))

        //val t: Toolbar = find<Toolbar>(R.id.toolbar)
        //val v: TextView = findViewById(R.id.toolbar_title) as TextView
        val l = LayoutInflater.from(this).inflate(R.layout.activity_main, null, false)
        //val toolbar: Toolbar = l.findViewById<Toolbar>(R.id.toolbar)
        //println(toolbar)
        val toolbar_title: TextView = l.findViewById<TextView>(R.id.toolbar_title)
        toolbar_title.text = "登入"
        supportActionBar!!.setCustomView(toolbar_title)
    }

    fun loginSubmit(view: View) {

    }

    fun loginFBSubmit(view: View) {

    }

    fun loginForgetPassword(view: View) {

    }

    fun loginRegister(view: View) {
        goRegister()
    }
}
