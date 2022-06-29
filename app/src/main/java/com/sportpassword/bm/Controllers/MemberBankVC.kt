package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.R

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

}