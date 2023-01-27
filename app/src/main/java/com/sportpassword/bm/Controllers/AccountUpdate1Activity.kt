package com.sportpassword.bm.Controllers

import android.app.Activity
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
import com.sportpassword.bm.databinding.ActivityAccountBinding
import com.sportpassword.bm.databinding.ActivityAccountUpdate1Binding
import java.util.*

class AccountUpdate1Activity : BaseActivity() {

    var field = ""
    var value = ""

    private lateinit var binding: ActivityAccountUpdate1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountUpdate1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        hidekeyboard(binding.testa)

        field = intent.getStringExtra("field")!!
        //value = member.fetch(field)

        binding.accountTxt.visibility = View.INVISIBLE
        binding.accountRadioGroup.visibility = View.INVISIBLE
        binding.accountDate.visibility = View.INVISIBLE

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
        binding.accountRadioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{ group, checkedId ->
            val radio = findViewById<RadioButton>(checkedId)
            val sex = MEMBER_SEX.from(radio.text.toString()).toString()
            value = sex
        })
    }

    fun radioForm() {
        binding.accountRadioGroup.visibility = View.VISIBLE
        binding.accountDate.visibility = View.INVISIBLE
        binding.accountTxt.visibility = View.INVISIBLE
        binding.clearbuttonAccount.visibility = View.INVISIBLE
        binding.accountRadioYes.text = "先生"
        binding.accountRadioNo.text = "小姐"
        if (value == MEMBER_SEX.M.toString()) binding.accountRadioYes.isChecked=true else binding.accountRadioNo.isChecked=true
    }

    fun dobForm() {
        binding.accountRadioGroup.visibility = View.INVISIBLE
        binding.accountDate.visibility = View.VISIBLE
        binding.accountTxt.visibility = View.INVISIBLE
        binding.clearbuttonAccount.visibility = View.INVISIBLE
        if (value.length == 0) {
            value = "2000-01-01"
        }

        val dobs = value.split("-").map{it.toInt()}
        //println(dobs)
        //val startDate = Date("1920-01-01")
        val cal = Calendar.getInstance()
        binding.accountDate.maxDate = cal.timeInMillis
        cal.set(Calendar.YEAR, 1920)
        cal.set(Calendar.MONTH, Calendar.JANUARY)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        binding.accountDate.minDate = cal.timeInMillis

        binding.accountDate.init(dobs[0], dobs[1]-1, dobs[2], DatePicker.OnDateChangedListener{ datePicker, y, m, d ->
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
        binding.accountRadioGroup.visibility = View.INVISIBLE
        binding.accountDate.visibility = View.INVISIBLE
        binding.accountTxt.visibility = View.VISIBLE
        binding.clearbuttonAccount.visibility = View.VISIBLE
        binding.accountTxt.setText(value)
        binding.accountTxt.requestFocus()
        binding.accountTxt.setSelection(value.length)
    }

    fun accountSubmit(view: View) {
//        Loading.show(mask)
        if (binding.accountTxt.visibility == View.VISIBLE) {
            value = binding.accountTxt.text.toString()
            //println(value)
        }
//        MemberService.update(this, member.id, field, value) { success ->
//            //println(success)
//            if (success) {
//                if (MemberService.success) {
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChangeIntent)
//                    finish()
//                } else {
//                    Alert.show(this, "警告", MemberService.msg)
//                }
//            } else {
//                Alert.show(this, "警告", MemberService.msg)
//            }
//        }
    }

    fun clear(view: View) {
        binding.accountTxt.setText("")
    }
}
