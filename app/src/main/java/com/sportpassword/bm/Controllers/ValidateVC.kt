package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.dump
import com.sportpassword.bm.Utilities.jsonToModel
import com.sportpassword.bm.Views.MainEditText2
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.Views.SubmitDelegate
import com.sportpassword.bm.Views.SubmitOnly
import com.sportpassword.bm.databinding.ActivityValidateBinding
import com.sportpassword.bm.member
import org.jetbrains.anko.runOnUiThread

class ValidateVC : BaseActivity(), SubmitDelegate {

    private lateinit var binding: ActivityValidateBinding
    private lateinit var view: ViewGroup

    var type: String = ""

    var showTop2: ShowTop2? = null
    var emailET2: MainEditText2? = null
    var codeET2: MainEditText2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityValidateBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")!!
        }

        findViewById<ShowTop2>(R.id.top) ?. let {
            showTop2 = it
            it.showPrev(true)
            if (type == "email") {
                it.setTitle("Email認證")
            } else if (type == "mobile") {
                it.setTitle("手機認證")
            }
        }

        findViewById<MainEditText2>(R.id.emailET2) ?. let {
            emailET2 = it
            if (type == "email") {
                it.setLabel("Email")
                it.setValue(member.email!!)
                it.setIcon("ic_email_svg")
            } else if (type == "mobile") {
                it.setLabel("手機號碼")
                it.setValue(member.mobile!!)
                it.setIcon("ic_mobile_svg")
            }
        }

        findViewById<MainEditText2>(R.id.codeET2) ?. let {
            codeET2 = it
            it.requestFocus()
        }

        findViewById<SubmitOnly>(R.id.submitSO) ?. let {
            it.delegate = this
        }

//        if (type == "email") {
//            setMyTitle("email認證")
//            //typeTxt.text = "email"
//            binding.typeTxt.setMyText(member.email!!, "")
//            binding.typeTxt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
//            binding.typeTxt.inputType = InputType.TYPE_CLASS_TEXT
//        } else if (type == "mobile") {
//            setMyTitle("手機認證")
//            //typeText. = "手機"
//            binding.typeTxt.setMyText(member.mobile!!, "")
//            binding.typeTxt.inputType = InputType.TYPE_CLASS_PHONE
//            binding.typeTxt.inputType = InputType.TYPE_CLASS_NUMBER
//        }
        hidekeyboard(binding.validateLayout)
//        init()
    }

//    override fun init() {
//        isPrevIconShow = true
//        super.init()
//    }

    fun resend(view: View) {
        val value = codeET2!!.text.toString()
        if (value.isEmpty()) {
//            var msg = ""
//            if (type == "email") {
//                msg = "請填寫email"
//            } else if (type == "mobile") {
//                msg = "請填寫手機號碼"
//            }
            Alert.show(this, "警告", "請填寫認證碼")
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
                            runOnUiThread {
                                warning(e.localizedMessage!!)
                            }
                        }

                        var msg = ""
                        if (t != null) {
                            if (t.success) {
                                info("認證成功", "關閉") {
                                    loadingAnimation.start()
                                    dataService.getOne(this, hashMapOf("token" to member.token!!)) { success ->
                                        runOnUiThread {
                                            loadingAnimation.stop()
                                        }
                                        if (success) {
                                            //println(MemberService.jsonString)
                                            session.dump()
                                            val table =
                                                jsonToModel<MemberTable>(MemberService.jsonString)
                                            table?.toSession(this, true)
                                            session.dump()
                                            toMemberItem(MainMemberEnum.info.chineseName)
                                        }
                                    }
                                }
                            } else {
                                msg = "伺服器錯誤，無法解析伺服器傳回的訊息"
                            }
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

    override fun submit2() {
        _hideKeyboard(binding.validateLayout)
        val code = codeET2!!.text.toString()
        if (code.isEmpty()) {
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
                                info("認證成功", "關閉") {
                                    loadingAnimation.start()
                                    MemberService.getOne(this, hashMapOf("token" to member.token!!)) { success ->
                                        runOnUiThread {
                                            loadingAnimation.stop()
                                        }
                                        if (success) {
                                            val table =
                                                jsonToModel<MemberTable>(MemberService.jsonString)
                                            table?.toSession(this, true)
                                            toMemberItem(MainMemberEnum.info.chineseName)
                                        }
                                    }
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
}
