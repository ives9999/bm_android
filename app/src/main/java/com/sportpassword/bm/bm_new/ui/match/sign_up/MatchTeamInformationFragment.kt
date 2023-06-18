package com.sportpassword.bm.bm_new.ui.match.sign_up

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.sportpassword.bm.Views.MainEditText2
import com.sportpassword.bm.bm_new.ui.base.BaseFragment
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.databinding.FragmentMatchTeamInformationBinding
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class MatchTeamInformationFragment : BaseFragment<FragmentMatchTeamInformationBinding>() {

    companion object {
        const val TEAM_NAME = "teamName"
        const val CAPTAIN_NAME = "captainName"
        const val CAPTAIN_PHONE = "captainPhone"
        const val CAPTAIN_EMAIL = "captainEmail"
        const val CAPTAIN_LINE = "captainLine"

        fun newInstance() = MatchTeamInformationFragment()
    }

    private val vm by sharedStateViewModel<MatchSignUpVM>()

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMatchTeamInformationBinding =
        FragmentMatchTeamInformationBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            listOf(
                Pair(edtTeamName, TEAM_NAME),
                Pair(edtCaptainName, CAPTAIN_NAME),
                Pair(edtCaptainPhone, CAPTAIN_PHONE),
                Pair(edtCaptainEmail, CAPTAIN_EMAIL),
                Pair(edtCaptainLine, CAPTAIN_LINE),
            ).forEach {
                editSignUpInfo(it.first, it.second)
            }
        }
    }

    override fun bindFragmentListener(context: Context) {}

    override fun getViewModel(): BaseViewModel? = null

    private fun editSignUpInfo(
        editText: MainEditText2,
        type: String
    ) {
        editText.contentET?.doAfterTextChanged {
            vm.setSignUpInfo(type, it.toString())
        }
    }
}