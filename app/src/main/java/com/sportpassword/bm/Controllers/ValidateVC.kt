package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.dump
import com.sportpassword.bm.Utilities.jsonToModel
import com.sportpassword.bm.Views.*
import com.sportpassword.bm.databinding.ActivityValidateBinding
import com.sportpassword.bm.member

class ValidateVC : BaseActivity(), SubmitDelegate, CancelOnlyDelegate {

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

        findViewById<CancelOnly>(R.id.cancelCO) ?. let {
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

    override fun submit2() {
        _hideKeyboard(binding.validateLayout)
        val code = codeET2!!.text.toString()
        if (code.isEmpty()) {
            warning("請填寫認證碼")
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
                                            toMember()
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
                        warning(MemberService.msg)
                    }
                }
            }
        }
    }

    override fun cancel2() {
        val value = emailET2!!.text.toString()
        if (value.isEmpty()) {
            var msg = ""
            if (type == "email") {
                msg = "請填寫email"
            } else if (type == "mobile") {
                msg = "請填寫手機號碼"
            }
            warning(msg)
        } else {
            loadingAnimation.start()
            MemberService.sendVaildateCode(this, type, value, member.token!!) { success ->
                runOnUiThread {
                    loadingAnimation.stop()
                }
                if (success) {
                    var msg = ""
                    if (type == "email") {
                        msg = "已經將認證信寄到註冊的信箱了"
                    } else if (type == "mobile") {
                        msg = "已經將認證碼發送到註冊的手機了"
                    }
                    runOnUiThread {
                        info(msg)
                    }
                } else {
                    runOnUiThread {
                        warning(MemberService.msg)
                    }
                }
            }
        }
    }
}
