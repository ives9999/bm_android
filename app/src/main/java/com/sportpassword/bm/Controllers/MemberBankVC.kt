package com.sportpassword.bm.Controllers

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.jsonToModel
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_member_bank_vc.*
import kotlinx.android.synthetic.main.mask.*

class MemberBankVC : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_bank_vc)

        setMyTitle("會員銀行帳戶資訊")

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }
    fun submitButtonPressed(view: View) {

        msg = ""

        if (bankTF.text.isEmpty()) {
            msg += "沒有填寫銀行名稱\n"
        }
        if (branchTF.text.isEmpty()) {
            msg += "沒有填寫分行名稱\n"
        }
        if (bank_codeTF.text.isEmpty()) {
            msg += "沒有填寫銀行代碼\n"
        }
        if (accountTF.text.isEmpty()) {
            msg += "沒有填寫銀行帳號\n"
        }

        params["bank"] = bankTF.text.toString()
        params["branch"] = branchTF.text.toString()
        params["bank_code"] = bank_codeTF.text.toString()
        params["account"] = accountTF.text.toString()
        params["member_token"] = member.token!!

        params["do"] = "update"

        if (msg.isNotEmpty()) {
            warning(msg)
            return
        }

        Loading.show(mask)
        MemberService.bank(this, params) { success ->
            Loading.hide(mask)
            if (success) {
                try {
                    val successTable: SuccessTable? = jsonToModel<SuccessTable>(MemberService.jsonString)
                    if (successTable != null) {
                        if (successTable.success) {
                            runOnUiThread {
                                info("已經更新您銀行帳戶資料")
                            }
                        } else {
                            runOnUiThread {
                                warning(successTable.msg)
                            }
                        }
                    }
                } catch (e: JsonParseException) {
                    warning(e.localizedMessage!!)
                    //println(e.localizedMessage)
                }
            }
        }

    }

    fun cancelButtonPressed(view: View) {
        prev()
    }
}