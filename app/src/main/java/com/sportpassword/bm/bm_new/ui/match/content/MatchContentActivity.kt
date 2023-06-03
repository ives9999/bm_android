package com.sportpassword.bm.bm_new.ui.match.content

import android.os.Bundle
import com.sportpassword.bm.R
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.databinding.ActivityMatchContentBinding

class MatchContentActivity : BaseActivity<ActivityMatchContentBinding>() {

    companion object {
        const val MATCH_TITLE = "match_title"
    }

    private var title: String? = null

    override fun initViewBinding(): ActivityMatchContentBinding =
        ActivityMatchContentBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
        data.getString(MATCH_TITLE)?.let {
            title = it
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            tvName.text = title

            with(tblMatch) {
                addTab(newTab().setText(getString(R.string.match_content)))
                addTab(newTab().setText(getString(R.string.match_brochure)))
                addTab(newTab().setText(getString(R.string.match_sign_up)))
            }
        }
    }

    override fun getViewModel(): BaseViewModel? = null
}