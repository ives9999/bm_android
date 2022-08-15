package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.then

class MemberLevelUpPayVC : BaseActivity() {

    var name: String = ""
    var price: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_level_up_pay_vc)

        name = (intent.hasExtra("name") then { intent.getStringExtra("name") }) ?: ""
        price = (intent.hasExtra("price") then { intent.getIntExtra("price", 0) }) ?: 0

        setMyTitle(name)
    }
}