package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.jsonToModel
import com.sportpassword.bm.Utilities.website
import kotlinx.android.synthetic.main.activity_select_manager_vc.*
import kotlinx.android.synthetic.main.mask.*

class SelectManagerVC : SelectVC() {

    var selected: Int = 0
    var manager_id: Int = 0
    var manager_token: String = ""

    var managerTable: MemberTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_manager_vc)

        if (intent.hasExtra("manager_id")) {
            manager_id = intent.getIntExtra("manager_id", 0)
        }

        if (intent.hasExtra("manager_token")) {
            manager_token = intent.getStringExtra("manager_token")!!
        }

        if (intent.hasExtra("able_type")) {
            able_type = intent.getStringExtra("able_type")!!
        }

        setMyTitle("管理者")

        clear.setOnClickListener {
            manager_tokenTF.setText("")
        }

        cityBtn.setOnClickListener {
            if (manager_tokenTF.text.isEmpty()) {
                warning("請填管理者金鑰")
            } else {
                val manager_token: String = manager_tokenTF.text.toString()
                getMemberOne(manager_token)
            }
        }

        findViewById<LinearLayout>(R.id.question) ?. let {
            it.setOnClickListener {
                "https://bm.sportpassword.com/doc/member_private_key.html".website(this)
            }
        }

        //manager_tokenTF.setText("S86cZ3OKQ65LcDMT4pPpPhP8Zcp6JmF")

        memberStactView.visibility = View.INVISIBLE
        buttonStactView.visibility = View.INVISIBLE

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun getMemberOne(token: String) {

        params = hashMapOf("token" to token)
        Loading.show(mask)
        MemberService.getOne(this, params) { success ->
            runOnUiThread {
                Loading.hide(mask)
            }
            if (success) {
                var successTable: SuccessTable? = null
                try {
                    successTable = jsonToModel<SuccessTable>(MemberService.jsonString)
                } catch (e: JsonParseException) {
                    runOnUiThread {
                        warning(e.localizedMessage!!)
                    }
                    //println(e.localizedMessage)
                }
                if (successTable != null) {
                    if (successTable.success) {
                        managerTable = jsonToModel<MemberTable>(MemberService.jsonString)
                        if (managerTable != null) {
                            runOnUiThread {
                                manager_id = managerTable!!.id
                                manager_token = managerTable!!.token
                                nameLbl.text = managerTable!!.nickname
                                emailLbl.text = managerTable!!.email
                                mobileLbl.text = managerTable!!.mobile

                                memberStactView.visibility = View.VISIBLE
                                buttonStactView.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        runOnUiThread {
                            warning("管理員金曜錯誤，系統無此會員")
                        }
                    }
                } else {
                    runOnUiThread {
                        warning("解析伺服器所傳的字串失敗，請洽管理員")
                    }
                }
            }
        }
    }

    fun submitBtnPressed(view: View) {

        val intent = Intent()
        if (managerTable != null) {
            intent.putExtra("manager_id", managerTable!!.id)
            intent.putExtra("manager_token", managerTable!!.token)
            intent.putExtra("manager_nickname", managerTable!!.nickname)
            intent.putExtra("able_type", able_type)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}