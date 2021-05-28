package com.sportpassword.bm.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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
import com.sportpassword.bm.Utilities.memberDidChangeIntent
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_account_update1.*
import kotlinx.android.synthetic.main.tab.*
import java.util.*
import kotlinx.android.synthetic.main.mask.*

class AccountUpdate1Activity : BaseActivity() {

    var field = ""
    var value = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_update1)

        hidekeyboard(testa)

        field = intent.getStringExtra("field")!!
        value = member.fetch(field)

        accountTxt.visibility = View.INVISIBLE
        accountRadioGroup.visibility = View.INVISIBLE
        accountDate.visibility = View.INVISIBLE

        when (field) {
            "sex" -> {
                radioForm()
                setMyTitle("性別")
            }
            "dob" -> {
                dobForm()
                setMyTitle("生日")
            }
            "nickname" -> {
                textForm()
                setMyTitle("暱稱")
            }
            "name" -> {
                textForm()
                setMyTitle("姓名" +
                        "")
            }
            "email" -> {
                textForm()
                setMyTitle("email")
            }
            "mobile" -> {
                textForm()
                setMyTitle("行動電話")
            }
            "tel" -> {
                textForm()
                setMyTitle("市內電話")
            }
            else -> {
                textForm()
                setMyTitle("修改")
            }
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
        clearbutton_account.visibility = View.INVISIBLE
        accountRadioYes.text = "先生"
        accountRadioNo.text = "小姐"
        if (value == MEMBER_SEX.M.toString()) accountRadioYes.isChecked=true else accountRadioNo.isChecked=true
    }

    fun dobForm() {
        accountRadioGroup.visibility = View.INVISIBLE
        accountDate.visibility = View.VISIBLE
        accountTxt.visibility = View.INVISIBLE
        clearbutton_account.visibility = View.INVISIBLE
        if (value.length == 0) {
            value = "2000-01-01"
        }

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
        clearbutton_account.visibility = View.VISIBLE
        accountTxt.setText(value)
        accountTxt.requestFocus()
        accountTxt.setSelection(value.length)
    }

    fun accountSubmit(view: View) {
//        Loading.show(mask)
        if (accountTxt.visibility == View.VISIBLE) {
            value = accountTxt.text.toString()
            //println(value)
        }
        MemberService.update(this, member.id, field, value) { success ->
            //println(success)
            if (success) {
                if (MemberService.success) {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChangeIntent)
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
