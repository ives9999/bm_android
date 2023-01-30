package com.sportpassword.bm.Controllers

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.jsonToModel
import com.sportpassword.bm.databinding.ActivityMemberBankVcBinding
import com.sportpassword.bm.databinding.MytablevcBinding
import com.sportpassword.bm.member

class MemberBankVC : BaseActivity() {

    private lateinit var binding: ActivityMemberBankVcBinding
    private lateinit var view: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemberBankVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        setMyTitle("會員銀行帳戶資訊")

        findViewById<ImageView>(R.id.clear_bank) ?. let { clear ->
            clear.setOnClickListener {
                clearButtonPressed(binding.clearBank)
            }
        }

        findViewById<ImageView>(R.id.clear_branch) ?. let { clear ->
            clear.setOnClickListener {
                clearButtonPressed(clear)
            }
        }

        findViewById<ImageView>(R.id.clear_bank_code) ?. let { clear ->
            clear.setOnClickListener {
                clearButtonPressed(clear)
            }
        }

        findViewById<ImageView>(R.id.clear_account) ?. let { clear ->
            clear.setOnClickListener {
                clearButtonPressed(clear)
            }
        }

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()

        binding.bankTF.setText(member.bank)
        binding.branchTF.setText(member.branch)
        if (member.bank_code != null) {
            binding.bankCodeTF.setText(member.bank_code!!.toString())
        }
        binding.accountTF.setText(member.account)
    }

    fun submitButtonPressed(view: View) {

        msg = ""

        if (binding.bankTF.text.isEmpty()) {
            msg += "沒有填寫銀行名稱\n"
        }
        if (binding.branchTF.text.isEmpty()) {
            msg += "沒有填寫分行名稱\n"
        }
        if (binding.bankCodeTF.text.isEmpty()) {
            msg += "沒有填寫銀行代碼\n"
        }
        if (binding.accountTF.text.isEmpty()) {
            msg += "沒有填寫銀行帳號\n"
        }

        params["bank"] = binding.bankTF.text.toString()
        params["branch"] = binding.branchTF.text.toString()
        params["bank_code"] = binding.bankCodeTF.text.toString()
        params["account"] = binding.accountTF.text.toString()
        params["member_token"] = member.token!!

        params["do"] = "update"

        if (msg.isNotEmpty()) {
            warning(msg)
            return
        }

        loadingAnimation.start()
        MemberService.bank(this, params) { success ->
            loadingAnimation.stop()
            if (success) {
                try {
                    val successTable: SuccessTable? = jsonToModel<SuccessTable>(MemberService.jsonString)
                    if (successTable != null) {
                        if (successTable.success) {
                            runOnUiThread {
                                member.bank = params["bank"]
                                member.branch = params["branch"]
                                member.bank_code = params["bank_code"]?.toInt()
                                member.account = params["account"]
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

    private fun clearButtonPressed(view: View) {
        val tag = view.tag

        when (tag) {
            "1" -> {
                binding.bankTF.setText("")
            }
            "2" -> {
                binding.branchTF.setText("")
            }
            "3"-> {
                binding.bankCodeTF.setText("")
            }
            "4"-> {
                binding.accountTF.setText("")
            }
        }
    }
}