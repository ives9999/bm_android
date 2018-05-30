package com.sportpassword.bm.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.*
import android.widget.TextView
import com.sportpassword.bm.Models.MEMBER_SEX
import com.sportpassword.bm.R
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_account_update1.*

class AccountActivity : BaseActivity() {

    val default = "未提供"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setMyTitle("帳戶")

        //setData()

        //val title: TextView =

        accountNicknameRow.setOnClickListener { view ->
//            val accountUpdateIntent = Intent(this, AccountUpdate1Activity::class.java)
//            accountUpdateIntent.putExtra("type", "nickname")
//            startActivity(accountUpdateIntent)
            goIntent("nickname")
        }
        accountNameRow.setOnClickListener { view ->
            goIntent("name")
        }
        accountEmailRow.setOnClickListener { view ->
            goIntent("email")
        }
        accountSexRow.setOnClickListener { view ->
            goIntent("sex")
        }
        accountDobRow.setOnClickListener { view ->
            goIntent("dob")
        }
        accountMobileRow.setOnClickListener { view ->
            goIntent("mobile")
        }
        accountTelRow.setOnClickListener { view ->
            goIntent("tel")
        }
    }

    override fun onResume() {
        super.onResume()
        //println("aaccount")
        //startActivity(getIntent());
        setData()
    }

    private fun goIntent(field: String) {
        val intent = Intent(this, AccountUpdate1Activity::class.java)
        intent.putExtra("field", field)
        startActivity(intent)
    }

    private fun setData() {
        accountNicknameLbl.setMyText(member.nickname, default)
        accountNickname.setMyText(member.nickname, default)
        val sex = MEMBER_SEX.valueOf(member.sex)
        accountSexLbl.setMyText(sex.value, default)
        accountSex.setMyText(sex.value, default)
        accountName.setMyText(member.name, default)
        accountEmail.setMyText(member.email, default)
        accountDob.setMyText(member.dob, default)
        accountMobile.setMyText(member.mobile, default)
        accountTel.setMyText(member.tel, default)
        val res = member.validateShow(member.validate)
        accountValidate.text = res.joinToString(",")
        accountType.text = member.typeShow(member.type)
    }

}
