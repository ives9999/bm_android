package com.sportpassword.bm.bm_new.ui.match.sign_up

import android.os.Bundle
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.databinding.ActivityMatchSignUpBinding

class MatchSignUpActivity : BaseActivity<ActivityMatchSignUpBinding>() {


    override fun initViewBinding(): ActivityMatchSignUpBinding =
        ActivityMatchSignUpBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {}

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            btnClose.setOnClickListener { finish() }

        }
    }

    override fun getViewModel(): BaseViewModel? = null

}