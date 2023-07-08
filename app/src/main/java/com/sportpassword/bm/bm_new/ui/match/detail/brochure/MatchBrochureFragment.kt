package com.sportpassword.bm.bm_new.ui.match.detail.brochure

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sportpassword.bm.bm_new.ui.base.BaseFragment
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.match.detail.MatchDetailVM
import com.sportpassword.bm.databinding.FragmentMatchBrochureBinding
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

//報名-簡章
class MatchBrochureFragment : BaseFragment<FragmentMatchBrochureBinding>() {

    companion object {
        fun newInstance() =
            MatchBrochureFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val vm by sharedStateViewModel<MatchDetailVM>()

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMatchBrochureBinding = FragmentMatchBrochureBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            vm.matchDetail.observe(viewLifecycleOwner) {
                brochure.loadData(it.content, "text/html", "utf-8")
            }
        }
    }

    override fun bindFragmentListener(context: Context) {
    }

    override fun getViewModel(): BaseViewModel? = null
}