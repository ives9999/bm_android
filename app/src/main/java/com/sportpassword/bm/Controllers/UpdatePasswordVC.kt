package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Views.MainEditText2
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.Views.SubmitDelegate
import com.sportpassword.bm.Views.SubmitOnly
import com.sportpassword.bm.databinding.ActivityUpdatePasswordBinding

class UpdatePasswordVC : BaseActivity(), SubmitDelegate {

    private lateinit var binding: ActivityUpdatePasswordBinding
    private lateinit var view: ViewGroup

    var showTop2: ShowTop2? = null
    var oldPasswordET2: MainEditText2? = null
    var newPasswordET2: MainEditText2? = null
    var rePasswordET2: MainEditText2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        findViewById<ShowTop2>(R.id.top) ?. let {
            showTop2 = it
            it.setTitle("更改密碼")
            it.showPrev(true)
        }

        findViewById<MainEditText2>(R.id.oldPasswordET2) ?. let {
            oldPasswordET2 = it
            it.requestFocus()
        }

        findViewById<MainEditText2>(R.id.newPasswordET2) ?. let {
            newPasswordET2 = it
        }

        findViewById<MainEditText2>(R.id.rePasswordET2) ?. let {
            rePasswordET2 = it
        }

        findViewById<SubmitOnly>(R.id.submitSO) ?. let {
            it.delegate = this
        }

//        setMyTitle("更改密碼")
        hidekeyboard(binding.updatePasswordLayout)
//        binding.updatePasswordOldPasswordTxt.requestFocus()

        //init()
    }

//    override fun init() {
//        isPrevIconShow = true
//        super.init()
//    }

    override fun submit2() {
        var msg: String = ""
        val oldPwd: String = oldPasswordET2!!.text
        if (oldPwd.isEmpty()) {
            msg += "舊密碼沒填\n"
            //Alert.show(this, "警告", "舊密碼沒填")
        }
        val newPwd: String = newPasswordET2!!.text
        if (newPwd.isEmpty()) {
            msg += "新密碼沒填\n"
            //Alert.show(this, "警告", "新密碼沒填")
        }
        val rePwd: String = rePasswordET2!!.text
        if (newPwd != rePwd) {
            msg += "新密碼不符合"
            //Alert.show(this, "警告", "新密碼不符合")
        }
        loadingAnimation.start()
        MemberService.changePassword(this, oldPwd, newPwd, rePwd) { success ->
            runOnUiThread {
                loadingAnimation.stop()
            }
            if (success) {

//                println(MemberService.jsonString)
                var t: SuccessTable? = null
                try {
                    t = Gson().fromJson<SuccessTable>(MemberService.jsonString, SuccessTable::class.java)
                } catch (e: JsonParseException) {
                    warning(e.localizedMessage!!)
                }

                if (t != null) {
                    if (t.success) {
                        runOnUiThread {
                            info("修改密碼成功，之後請用新密碼登入", "", "關閉") {
                                toLogin()
//                                val intent = Intent()
//                                setResult(Activity.RESULT_OK, intent)
//                                finish()
                            }
                        }
                    } else {
                        runOnUiThread {
                            warning(t.msg)
                        }
                    }
                } else {
                    runOnUiThread {
                        warning("解析回傳值失敗")
                    }
                }

//                if (MemberService.success) {
//                    Alert.show(this, "訊息", MemberService.msg) {
//                        home(this)
//                    }
//                } else {
//                    Alert.show(this, "警告", MemberService.msg)
//                }
            } else {
                runOnUiThread {
                    Alert.show(this, "警告", MemberService.msg)
                }
            }
        }
    }
}
