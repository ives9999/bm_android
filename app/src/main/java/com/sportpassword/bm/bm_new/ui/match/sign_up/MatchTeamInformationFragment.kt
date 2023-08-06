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
import com.sportpassword.bm.member
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class MatchTeamInformationFragment : BaseFragment<FragmentMatchTeamInformationBinding>() {

    companion object {
        const val TEAM_NAME = "teamName"
        const val CAPTAIN = 0
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
            btnClose.setOnClickListener {
                requireActivity().finish()
            }

            listOf(
                Pair(edtTeamName, TEAM_NAME),
                Pair(edtCaptainName, CAPTAIN_NAME),
                Pair(edtCaptainPhone, CAPTAIN_PHONE),
                Pair(edtCaptainEmail, CAPTAIN_EMAIL),
                Pair(edtCaptainLine, CAPTAIN_LINE),
            ).forEach {
                editSignUpInfo(it.first, it.second)
            }

            vm.matchSignUp.observe(viewLifecycleOwner) {
                edtTeamName.contentET?.setText(it.name)

                //add by ives 2023/08/06 如果是報名，自動帶入隊長的會員資料，避免再填寫一次
                var managerName: String = member.name!!
                if (!it.managerName.isNullOrEmpty()) {
                    managerName = it.managerName
                }
                edtCaptainName.contentET?.setText(managerName)

                var managerMobile: String = member.mobile!!
                if (!it.managerMobile.isNullOrEmpty()) {
                    managerMobile = it.managerMobile
                }
                edtCaptainPhone.contentET?.setText(managerMobile)

                var managerEmail: String = member.email!!
                if (!it.managerEmail.isNullOrEmpty()) {
                    managerEmail = it.managerEmail
                }
                edtCaptainEmail.contentET?.setText(managerEmail)

                var managerLine: String = member.line!!
                if (!it.managerLine.isNullOrEmpty()) {
                    managerLine = it.managerLine
                }
                edtCaptainLine.contentET?.setText(managerLine)

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
            vm.setSignUpInfo(CAPTAIN, type, it.toString())
        }
    }
}