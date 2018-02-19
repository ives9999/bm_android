package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Models.MEMBER_SEX
import com.sportpassword.bm.R
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : BaseActivity() {

    val default = "未提供"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setData()
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
        accountValidate.text = member.validateShow(member.validate)
        accountType.text = member.typeShow(member.type)
    }

}
