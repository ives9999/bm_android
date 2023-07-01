package com.sportpassword.bm.bm_new.ui.match.sign_up

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.sportpassword.bm.R
import com.sportpassword.bm.Views.MainEditText2
import com.sportpassword.bm.bm_new.ui.base.BaseFragment
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.databinding.FragmentMatchTeamPlayerBinding
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class MatchTeamPlayerFragment : BaseFragment<FragmentMatchTeamPlayerBinding>() {

    companion object {
        const val IS_PLAYER_ONE = "is_player_one"
        const val PLAYER_ONE_NAME = "player_one_name"
        const val PLAYER_ONE_PHONE = "player_one_phone"
        const val PLAYER_ONE_EMAIL = "player_one_email"
        const val PLAYER_ONE_LINE = "player_one_line"
        const val PLAYER_ONE_AGE = "player_one_age"
        const val PLAYER_TWO_NAME = "player_two_name"
        const val PLAYER_TWO_PHONE = "player_two_phone"
        const val PLAYER_TWO_EMAIL = "player_two_email"
        const val PLAYER_TWO_LINE = "player_two_line"
        const val PLAYER_TWO_AGE = "player_two_age"

        fun newInstance(isPlayerOne: Boolean) = MatchTeamPlayerFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_PLAYER_ONE, isPlayerOne)
            }
        }
    }

    private val vm by sharedStateViewModel<MatchSignUpVM>()
    private var isPlayerOne = true

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMatchTeamPlayerBinding =
        FragmentMatchTeamPlayerBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
        isPlayerOne = data.getBoolean(IS_PLAYER_ONE)
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {

            btnClose.setOnClickListener {
                requireActivity().finish()
            }

            tvTitle.text = getString(
                if (isPlayerOne) R.string.match_sign_player_one
                else R.string.match_sign_player_two
            )

            listOf(
                Pair(edtName, if (isPlayerOne) PLAYER_ONE_NAME else PLAYER_TWO_NAME),
                Pair(edtPhone, if (isPlayerOne) PLAYER_ONE_PHONE else PLAYER_TWO_PHONE),
                Pair(edtEmail, if (isPlayerOne) PLAYER_ONE_EMAIL else PLAYER_TWO_EMAIL),
                Pair(edtLine, if (isPlayerOne) PLAYER_ONE_LINE else PLAYER_TWO_LINE),
                Pair(edtAge, if (isPlayerOne) PLAYER_ONE_AGE else PLAYER_TWO_AGE),
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