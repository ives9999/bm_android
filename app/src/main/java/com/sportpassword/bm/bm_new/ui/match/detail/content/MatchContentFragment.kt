package com.sportpassword.bm.bm_new.ui.match.detail.content

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sportpassword.bm.bm_new.ui.base.BaseFragment
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.match.detail.MatchDetailVM
import com.sportpassword.bm.databinding.FragmentMatchContentBinding
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class MatchContentFragment : BaseFragment<FragmentMatchContentBinding>() {

    companion object {
        fun newInstance() =
            MatchContentFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val vm by sharedStateViewModel<MatchDetailVM>()

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMatchContentBinding = FragmentMatchContentBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            vm.matchDetail.observe(viewLifecycleOwner) {
                //比賽時間
                tvGameStartDate.text = it.matchStart.split(" ").getOrNull(0)
                tvGameStartTime.text = it.matchStart.split(" ").getOrNull(1)
                tvGameEndDate.text = it.matchEnd.split(" ").getOrNull(0)
                tvGameEndTime.text = it.matchEnd.split(" ").getOrNull(1)
                //報名時間
                tvSignStartDate.text = it.signupStart.split(" ").getOrNull(0)
                tvSignStartTime.text = it.signupStart.split(" ").getOrNull(1)
                tvSignEndDate.text = it.signupEnd.split(" ").getOrNull(0)
                tvSignEndTime.text = it.signupEnd.split(" ").getOrNull(1)
                //球館
                tvArena.text = it.arena.name
                tvAddress.text = it.arena.road
                tvLocationPhone.text = it.arena.tel
                tvBadminton.text = it.ball
                //聯絡人
                tvPerson.text = it.matchContact.contactName
                tvPersonPhone.text = it.matchContact.contactTel
                tvEmail.text = it.matchContact.contactEmail
            }
        }
    }

    override fun bindFragmentListener(context: Context) {
    }

    override fun getViewModel(): BaseViewModel? = null
}