package com.sportpassword.bm.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.sportpassword.bm.Models.MEMBER_SEX
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.NOTIF_MEMBER_DID_CHANGE
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_account_update1.*
import kotlinx.android.synthetic.main.tab.*
import java.util.*

class AccountUpdate1Activity : BaseActivity() {

    var field = ""
    var value = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_update1)

        hidekyboard(testa)

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
        accountRadioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{ group, checkedId ->
            val radio = findViewById<RadioButton>(checkedId)
            val sex = MEMBER_SEX.from(radio.text.toString()).toString()
            value = sex
        })
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

        accountDate.init(dobs[0], dobs[1]-1, dobs[2], DatePicker.OnDateChangedListener{ datePicker, y, m, d ->
            //println("y:$y, m:$m, d:$d")
            value = "$y-${m+1}-$d"
            //println(value)

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
        accountTxt.requestFocus()
        accountTxt.setSelection(value.length)
    }

    fun accountSubmit(view: View) {
        val loading = Loading.show(this)
        if (accountTxt.visibility == View.VISIBLE) {
            value = accountTxt.text.toString()
            //println(value)
        }
        MemberService.update(this, member.id, field, value) { success ->
            loading.dismiss()
            //println(success)
            if (success) {
                if (MemberService.success) {
                    val memberDidChange = Intent(NOTIF_MEMBER_DID_CHANGE)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChange)
                    finish()
                } else {
                    Alert.show(this, "警告", MemberService.msg)
                }
            } else {
                Alert.show(this, "警告", MemberService.msg)
            }
        }
    }

    fun clear(view: View) {
        accountTxt.setText("")
    }
}
