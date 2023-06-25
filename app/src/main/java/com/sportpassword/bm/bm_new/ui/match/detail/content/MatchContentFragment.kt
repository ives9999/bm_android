package com.sportpassword.bm.bm_new.ui.match.detail.content

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sportpassword.bm.bm_new.ui.base.BaseFragment
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.match.detail.MatchDetailVM
import com.sportpassword.bm.bm_new.ui.util.Zone
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
                matchStartDate.setContent(it.matchStart.dropLast(3))
                matchEndDate.setContent(it.matchEnd.dropLast(3))
                //報名時間
                signStartDate.setContent(it.signupStart.dropLast(3))
                signEndDate.setContent(it.signupEnd.dropLast(3))
                //球館
                matchCity.setContent(Zone.cityIdToString(it.arena.cityId))
                matchArena.setContent(it.arena.name)
                matchArenaAddress.setContent(it.arena.road)
                matchArenaPhone.setContent(it.arena.tel)
                matchBall.setContent(it.ball)
                //聯絡人
                matchContactPerson.setContent(it.matchContact.contactName)
                matchContactPhone.setContent(it.matchContact.contactTel)
                matchContactEmail.setContent(it.matchContact.contactEmail)
            }
        }
    }

    override fun bindFragmentListener(context: Context) {
    }

    override fun getViewModel(): BaseViewModel? = null
}