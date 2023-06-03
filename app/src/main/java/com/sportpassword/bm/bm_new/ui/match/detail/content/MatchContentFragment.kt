package com.sportpassword.bm.bm_new.ui.match.detail.content

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sportpassword.bm.bm_new.ui.base.BaseFragment
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.databinding.FragmentMatchContentBinding

class MatchContentFragment : BaseFragment<FragmentMatchContentBinding>() {

    companion object {
        fun newInstance() =
            MatchContentFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMatchContentBinding = FragmentMatchContentBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun bindFragmentListener(context: Context) {
    }

    override fun getViewModel(): BaseViewModel? = null
}