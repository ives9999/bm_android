package com.sportpassword.bm.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import android.view.*
import android.widget.TextView
import com.sportpassword.bm.Models.MEMBER_SEX
import com.sportpassword.bm.R
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_account_update1.*
import kotlinx.android.synthetic.main.mask.*

class AccountActivity : BaseActivity() {

    val default = "未提供"
    val ACCOUNT_REQUEST_CODE = 1

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

        refreshLayout = account_refresh
        setRefreshListener()
    }

//    fun refresh() {
//        member.token?.let {
//            _getMemberOne(it) {
//            setData()
//            closeRefresh()
//        }
//        }
//    }

    override fun onResume() {
        super.onResume()
        //println("aaccount")
        //startActivity(getIntent());
        setData()
    }

    private fun goIntent(field: String) {
        val intent = Intent(this, AccountUpdate1Activity::class.java)
        intent.putExtra("field", field)
        startActivityForResult(intent, ACCOUNT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            ACCOUNT_REQUEST_CODE -> {
//                member.token?.let {
//                    _getMemberOne(it) {
//                        setData()
//                    }
//                }
//            }
//        }
    }

    private fun setData() {
//        member.nickname?.let { accountNicknameLbl.setMyText(it, default) }
//        member.nickname?.let { accountNickname.setMyText(it, default) }
//        val sex = member.sex?.let { MEMBER_SEX.valueOf(it) }
//        if (sex != null) {
//            accountSexLbl.setMyText(sex.value, default)
//        }
//        if (sex != null) {
//            accountSex.setMyText(sex.value, default)
//        }
//        member.name?.let { accountName.setMyText(it, default) }
//        member.email?.let { accountEmail.setMyText(it, default) }
//        member.dob?.let { accountDob.setMyText(it, default) }
//        member.mobile?.let { accountMobile.setMyText(it, default) }
//        println("tel:" + member.tel)
//        member.tel?.let { accountTel.setMyText(it, default) }
//        val res = member.validateShow(member.validate)
//        accountValidate.text = res.joinToString(",")
//        accountType.text = member.typeShow(member.type)
    }

}
