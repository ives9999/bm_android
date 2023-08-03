package com.sportpassword.bm.bm_new.ui.match.sign_up

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.internal.ViewUtils.dpToPx
import com.sportpassword.bm.R
import com.sportpassword.bm.Views.AttributesView
import com.sportpassword.bm.Views.MainEditText2
import com.sportpassword.bm.bm_new.ui.base.BaseFragment
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_AGE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_EMAIL
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_LINE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_NAME
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_PHONE
import com.sportpassword.bm.databinding.FragmentMatchTeamPlayerBinding
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import timber.log.Timber
import tw.com.bluemobile.hbc.utilities.getColor

class MatchTeamPlayerFragment : BaseFragment<FragmentMatchTeamPlayerBinding>(),
    AttributesView.Listener {

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

    @SuppressLint("RestrictedApi")
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
                editSignUpInfo(it.first, it.second)
            }

            vm.matchSignUp.observe(viewLifecycleOwner) {

                //add by ives 2023/08/02
                //當賽事沒有贈品時，贈品與分隔線就不出現
                if (it.matchGifts.isEmpty()) {
                    line1.visibility = View.GONE
                    tvGiveaway.visibility = View.GONE
                }

                it.matchGifts.getOrNull(0)?.let { matchGift ->
                    val marginTopInPixels = dpToPx(requireContext(), 16)
                    val linearParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    matchGift.product.productAttributes.forEach { gift ->
                        //加Gift name
                        TextView(requireContext()).apply {
                            setTextColor(getColor(requireContext(), R.color.TEXT_WHITE))
                            setTypeface(null, Typeface.BOLD)
                            text = gift.name
                            linearParams.topMargin = marginTopInPixels.toInt()
                            layoutParams = linearParams
                            llGift.addView(this)
                        }
                        //加Gift選項
                        AttributesView(requireContext()).apply {
                            if (it.matchPlayers.isNotEmpty()) {
                                //有已報名隊員資料，爲修改
                                it.matchPlayers.getOrNull(playerNum - 1)?.let { player ->
                                    player.matchPlayerGifts.getOrNull(0)?.let { matchPlayerGift ->
                                        matchPlayerGift.attributes.split("|")
                                            .find { it.contains(gift.alias) }?.let { attribute ->
                                            //設置已選贈品
                                            vm.setSignUpInfo(playerNum, gift.alias, attribute)
                                            findValue(attribute)?.let {
                                                setAttributes(
                                                    gift,
                                                    selected = it,
                                                    listener = this@MatchTeamPlayerFragment
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                //新的報名
                                setAttributes(gift, listener = this@MatchTeamPlayerFragment)
                            }
                            llGift.addView(this)
                        }
                    }
                }

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
        editText: MainEditText2,
        type: String
    ) {
        editText.contentET?.doAfterTextChanged {
            vm.setSignUpInfo(playerNum, type, it.toString())
        }
    }

    override fun onTagClick(alias: String, giftData: String) {
        Timber.d("onTagClick() $giftData")
        vm.setSignUpInfo(playerNum, alias, giftData)
    }

    private fun findValue(input: String): String? {
        try {
            val jsonObject = JSONObject(input)
            return jsonObject.getString("value")
        } catch (e: Exception) {
            Timber.d("findValue error:$e")
        }
        return null
    }
}