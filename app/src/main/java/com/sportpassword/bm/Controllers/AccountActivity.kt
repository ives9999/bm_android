package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.*
import com.sportpassword.bm.databinding.ActivityAccountBinding

class AccountActivity : BaseActivity() {

    val default = "未提供"
    val ACCOUNT_REQUEST_CODE = 1

    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setMyTitle("帳戶")

        //setData()

        //val title: TextView =

        binding.accountNicknameRow.setOnClickListener {
//            val accountUpdateIntent = Intent(this, AccountUpdate1Activity::class.java)
//            accountUpdateIntent.putExtra("type", "nickname")
//            startActivity(accountUpdateIntent)
            goIntent("nickname")
        }
        binding.accountNameRow.setOnClickListener {
            goIntent("name")
        }
        binding.accountEmailRow.setOnClickListener {
            goIntent("email")
        }
        binding.accountSexRow.setOnClickListener {
            goIntent("sex")
        }
        binding.accountDobRow.setOnClickListener {
            goIntent("dob")
        }
        binding.accountMobileRow.setOnClickListener {
            goIntent("mobile")
        }
        binding.accountTelRow.setOnClickListener {
            goIntent("tel")
        }

        refreshLayout = binding.accountRefresh
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

    @Deprecated("Deprecated in Java")
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
