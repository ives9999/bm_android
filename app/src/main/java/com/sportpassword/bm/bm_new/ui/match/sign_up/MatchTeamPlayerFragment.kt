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
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_AGE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_EMAIL
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_LINE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_NAME
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_PHONE
import com.sportpassword.bm.databinding.FragmentMatchTeamPlayerBinding
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class MatchTeamPlayerFragment : BaseFragment<FragmentMatchTeamPlayerBinding>() {

    companion object {
        private const val PLAYER_NUM = "player_number"

        fun newInstance(playerNum: Int) = MatchTeamPlayerFragment().apply {
            arguments = Bundle().apply {
                putInt(PLAYER_NUM, playerNum)
            }
        }
    }

    private val vm by sharedStateViewModel<MatchSignUpVM>()
    private val playerNum get() = requireArguments().getInt(PLAYER_NUM, 1)

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMatchTeamPlayerBinding =
        FragmentMatchTeamPlayerBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {}

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {

            btnClose.setOnClickListener {
                requireActivity().finish()
            }

            tvTitle.text = getString(R.string.match_sign_player_num, playerNum)

            listOf(
                Pair(edtName, PLAYER_NAME),
                Pair(edtPhone, PLAYER_PHONE),
                Pair(edtEmail, PLAYER_EMAIL),
                Pair(edtLine, PLAYER_LINE),
                Pair(edtAge, PLAYER_AGE),
            ).forEach {
                editSignUpInfo(playerNum, it.first, it.second)
            }

            vm.matchSignUp.observe(viewLifecycleOwner) {
                val gifts = it.matchGifts
                val gift = gifts[0].product.productAttributes[0].attribute
                tagContainer.setAttributes(gift)

                it.matchPlayers.getOrNull(playerNum - 1)?.let { player ->
                    edtName.contentET?.setText(player.name)
                    edtPhone.contentET?.setText(player.mobile)
                    edtEmail.contentET?.setText(player.email)
                    edtLine.contentET?.setText(player.line)
                    edtAge.contentET?.setText(player.age.toString())
                }
            }
        }
    }

    override fun bindFragmentListener(context: Context) {}

    override fun getViewModel(): BaseViewModel? = null

    private fun editSignUpInfo(
        playerNum: Int,
        editText: MainEditText2,
        type: String
    ) {
        editText.contentET?.doAfterTextChanged {
            vm.setSignUpInfo(playerNum, type, it.toString())
        }
    }
}