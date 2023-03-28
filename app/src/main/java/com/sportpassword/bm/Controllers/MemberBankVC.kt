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
import com.sportpassword.bm.Utilities.jsonToModel
import com.sportpassword.bm.Views.MainEditText2
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.Views.SubmitDelegate
import com.sportpassword.bm.Views.SubmitOnly
import com.sportpassword.bm.databinding.ActivityMemberBankVcBinding
import com.sportpassword.bm.databinding.MytablevcBinding
import com.sportpassword.bm.member

class MemberBankVC : BaseActivity(), SubmitDelegate {

    private lateinit var binding: ActivityMemberBankVcBinding
    private lateinit var view: ViewGroup

    var showTop2: ShowTop2? = null
    var nameET2: MainEditText2? = null
    var branchET2: MainEditText2? = null
    var codeET2: MainEditText2? = null
    var accountET2: MainEditText2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemberBankVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        findViewById<ShowTop2>(R.id.top) ?. let {
            showTop2 = it
            it.showPrev(true)
            it.setTitle("會員銀行帳戶")
        }

        findViewById<MainEditText2>(R.id.nameET2) ?. let {
            nameET2 = it
            it.requestFocus()
        }

        findViewById<MainEditText2>(R.id.branchET2) ?. let {
            branchET2 = it
        }

        findViewById<MainEditText2>(R.id.codeET2) ?. let {
            codeET2 = it
        }

        findViewById<MainEditText2>(R.id.accountET2) ?. let {
            accountET2 = it
        }

        findViewById<SubmitOnly>(R.id.submitSO) ?. let {
            it.delegate = this
        }



//        setMyTitle("會員銀行帳戶資訊")

//        findViewById<ImageView>(R.id.clear_bank) ?. let { clear ->
//            clear.setOnClickListener {
//                clearButtonPressed(binding.clearBank)
//            }
//        }
//
//        findViewById<ImageView>(R.id.clear_branch) ?. let { clear ->
//            clear.setOnClickListener {
//                clearButtonPressed(clear)
//            }
//        }
//
//        findViewById<ImageView>(R.id.clear_bank_code) ?. let { clear ->
//            clear.setOnClickListener {
//                clearButtonPressed(clear)
//            }
//        }
//
//        findViewById<ImageView>(R.id.clear_account) ?. let { clear ->
//            clear.setOnClickListener {
//                clearButtonPressed(clear)
//            }
//        }

        init()
    }

    override fun init() {

        nameET2?.setValue(member.bank!!)
        branchET2?.setValue(member.branch!!)
        if (member.bank_code != null) {
            codeET2?.setValue(member.bank_code!!.toString())
        }
        accountET2?.setValue(member.account!!)
    }

    override fun submit2() {

        msg = ""

        if (nameET2!!.text.isEmpty()) {
            msg += "沒有填寫銀行名稱\n"
        }
        if (branchET2!!.text.isEmpty()) {
            msg += "沒有填寫分行名稱\n"
        }
        if (codeET2!!.text.isEmpty()) {
            msg += "沒有填寫銀行代碼\n"
        }
        if (accountET2!!.text.isEmpty()) {
            msg += "沒有填寫銀行帳號\n"
        }

        params["bank"] = nameET2!!.text.toString()
        params["branch"] = branchET2!!.text.toString()
        params["bank_code"] = codeET2!!.text.toString()
        params["account"] = accountET2!!.text.toString()
        params["member_token"] = member.token!!

        params["do"] = "update"

        if (msg.isNotEmpty()) {
            warning(msg)
            return
        }

        loadingAnimation.start()
        MemberService.bank(this, params) { success ->
            runOnUiThread {
                loadingAnimation.stop()
            }
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

//    fun cancelButtonPressed(view: View) {
//        prev()
//    }
//
//    private fun clearButtonPressed(view: View) {
//        val tag = view.tag
//
//        when (tag) {
//            "1" -> {
//                binding.bankTF.setText("")
//            }
//            "2" -> {
//                binding.branchTF.setText("")
//            }
//            "3"-> {
//                binding.bankCodeTF.setText("")
//            }
//            "4"-> {
//                binding.accountTF.setText("")
//            }
//        }
//    }
}