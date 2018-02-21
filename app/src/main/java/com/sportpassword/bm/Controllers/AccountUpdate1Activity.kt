package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View
import android.widget.DatePicker
import com.sportpassword.bm.Models.MEMBER_SEX
import com.sportpassword.bm.R
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_account_update1.*
import kotlinx.android.synthetic.main.tab.*
import java.util.*

class AccountUpdate1Activity : AppCompatActivity() {

    var field = ""
    var value = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_update1)

        field = intent.getStringExtra("field")
        value = member.fetch(field)

        accountTxt.visibility = View.INVISIBLE
        accountRadioGroup.visibility = View.INVISIBLE
        accountDate.visibility = View.INVISIBLE

        when (field) {
            "sex" -> radioForm()
            "dob" -> dobForm()
            else ->textForm()
        }
        //println(value)
    }

    fun radioForm() {
        accountRadioGroup.visibility = View.VISIBLE
        accountDate.visibility = View.INVISIBLE
        accountTxt.visibility = View.INVISIBLE
        accountRadioYes.text = "先生"
        accountRadioNo.text = "小姐"
        if (value == MEMBER_SEX.M.toString()) accountRadioYes.isChecked=true else accountRadioNo.isChecked=true
    }

    fun dobForm() {
        accountRadioGroup.visibility = View.INVISIBLE
        accountDate.visibility = View.VISIBLE
        accountTxt.visibility = View.INVISIBLE

        val dobs = value.split("-").map{it.toInt()}
        //println(dobs)
        //val startDate = Date("1920-01-01")
        val cal = Calendar.getInstance()
        accountDate.maxDate = cal.timeInMillis
        cal.set(Calendar.YEAR, 1920)
        cal.set(Calendar.MONTH, Calendar.JANUARY)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        accountDate.minDate = cal.timeInMillis

        accountDate.init(dobs[0], dobs[1], dobs[2], DatePicker.OnDateChangedListener{ datePicker, y, m, d ->

        })

        val layout = findViewById(R.id.testa) as ConstraintLayout;
        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)
        constraintSet.connect(R.id.accountSubmitBtn, ConstraintSet.TOP, R.id.accountDate, ConstraintSet.BOTTOM, 32)
        constraintSet.applyTo(layout)
    }

    fun textForm() {
        accountRadioGroup.visibility = View.INVISIBLE
        accountDate.visibility = View.INVISIBLE
        accountTxt.visibility = View.VISIBLE
        accountTxt.setText(value)
    }
}
