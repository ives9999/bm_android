package com.sportpassword.bm.bm_new.ui.match.team_list

import android.os.Bundle
import com.sportpassword.bm.R
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.databinding.ActivityMatchBinding

class MatchTeamListActivity : BaseActivity<ActivityMatchBinding>() {


    override fun initViewBinding(): ActivityMatchBinding =
        ActivityMatchBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {}

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            tvName.text = getString(R.string.match_manage)
        }
    }

    override fun getViewModel(): BaseViewModel? = null

}