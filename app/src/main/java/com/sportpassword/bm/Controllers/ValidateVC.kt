package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.databinding.ActivityValidateBinding
import com.sportpassword.bm.member

class ValidateVC : BaseActivity() {

    private lateinit var binding: ActivityValidateBinding
    private lateinit var view: ViewGroup

    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityValidateBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")!!
        }

        if (type == "email") {
            setMyTitle("email認證")
            //typeTxt.text = "email"
            binding.typeTxt.setMyText(member.email!!, "")
            binding.typeTxt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            binding.typeTxt.inputType = InputType.TYPE_CLASS_TEXT
        } else if (type == "mobile") {
            setMyTitle("手機認證")
            //typeText. = "手機"
            binding.typeTxt.setMyText(member.mobile!!, "")
            binding.typeTxt.inputType = InputType.TYPE_CLASS_PHONE
            binding.typeTxt.inputType = InputType.TYPE_CLASS_NUMBER
        }
        hidekeyboard(binding.validateLayout)
        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun submit(view: View) {
        _hideKeyboard(binding.validateLayout)
        val code = binding.codeTxt.text.toString()
        if (code.length == 0) {
            Alert.show(this, "警告", "請填寫認證碼")
        } else {
            loadingAnimation.start()
            MemberService.validate(this, type, code, member.token!!) { success ->
                runOnUiThread {
                    loadingAnimation.stop()
                }
                if (success) {

                    var t: SuccessTable? = null
                    try {
                        t = Gson().fromJson<SuccessTable>(MemberService.jsonString, SuccessTable::class.java)
                    } catch (e: JsonParseException) {
                        warning(e.localizedMessage!!)
                    }

                    if (t != null) {
                        if (t.success) {
                            runOnUiThread {
                                Alert.show(this, "訊息", "認證成功") {
                                    //LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChangeIntent)
                                    val intent = Intent()
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                }
                            }
                        } else {
                            runOnUiThread {
                                warning(t.msg)
                            }
                        }
                    }
                } else {
                    runOnUiThread {
                        Alert.show(this, "警告", MemberService.msg)
                    }
                }
            }
        }
    }

    fun resend(view: View) {
        val value = binding.codeTxt.text.toString()
        if (value.length <= 0) {
            var msg = ""
            if (type == "email") {
                msg = "請填寫email"
            } else if (type == "mobile") {
                msg = "請填寫手機號碼"
            }
            Alert.show(this, "警告", msg)
        } else {
            loadingAnimation.start()
            MemberService.sendVaildateCode(this, type, value, member.token!!) { success ->
                runOnUiThread {
                    loadingAnimation.stop()
                }
                if (success) {

                    if (success) {

                        var t: SuccessTable? = null
                        try {
                            t = Gson().fromJson<SuccessTable>(
                                MemberService.jsonString,
                                SuccessTable::class.java
                            )
                        } catch (e: JsonParseException) {
                            warning(e.localizedMessage!!)
                        }

                        var msg = ""
                        if (t != null) {
                            if (t.success) {
                                if (type == "email") {
                                    msg = "已經將認證信寄到註冊的信箱了"
                                } else if (type == "mobile") {
                                    msg = "已經將認證碼發送到註冊的手機了"
                                }
                            } else {
                                msg = "伺服器錯誤，無法解析伺服器傳回的訊息"
                            }
                        }

                        runOnUiThread {
                            info(msg)
                        }
                    }


//                    var msg = ""
//                    if (type == "email") {
//                        msg = "已經將認證信寄到註冊的信箱了"
//                    } else if (type == "mobile") {
//                        msg = "已經將認證碼發送到註冊的手機了"
//                    }
//                    runOnUiThread {
//                        Alert.show(this, "訊息", msg)
//                    }
                } else {
                    runOnUiThread {
                        Alert.show(this, "警告", MemberService.msg)
                    }
                }
            }
        }
    }
}
