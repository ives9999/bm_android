package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.databinding.ActivityUpdatePasswordBinding

class UpdatePasswordVC : BaseActivity() {

    private lateinit var binding: ActivityUpdatePasswordBinding
    private lateinit var view: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        setMyTitle("更改密碼")
        hidekeyboard(binding.updatePasswordLayout)
        binding.updatePasswordOldPasswordTxt.requestFocus()

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun updatePasswordSubmit(view: View) {
        val oldPwd = binding.updatePasswordOldPasswordTxt.text.toString()
        if (oldPwd.isEmpty()) {
            Alert.show(this, "警告", "舊密碼沒填")
        }
        val newPwd = binding.updatePasswordNewPasswordTxt.text.toString()
        if (newPwd.isEmpty()) {
            Alert.show(this, "警告", "新密碼沒填")
        }
        val rePwd = binding.updatePasswordRePasswordTxt.text.toString()
        if (newPwd != rePwd) {
            Alert.show(this, "警告", "新密碼不符合")
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
