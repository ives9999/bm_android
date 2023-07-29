package com.sportpassword.bm.bm_new.ui.match.detail.groups

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.bm_new.data.dto.match.MatchDetailDto
import com.sportpassword.bm.bm_new.ui.base.BaseFragment
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.match.detail.MatchDetailVM
import com.sportpassword.bm.bm_new.ui.util.LinearItemDecoration
import com.sportpassword.bm.bm_new.ui.util.canSignUp
import com.sportpassword.bm.bm_new.ui.util.toMatchSignUp
import com.sportpassword.bm.databinding.FragmentMatchGroupsBinding
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class MatchGroupsFragment : BaseFragment<FragmentMatchGroupsBinding>(),
    MatchGroupsAdapter.Listener {

    companion object {
        fun newInstance() =
            MatchGroupsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val vm by sharedStateViewModel<MatchDetailVM>()
    private var matchGroupsAdapter: MatchGroupsAdapter? = null

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMatchGroupsBinding = FragmentMatchGroupsBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {

            rv.run {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = MatchGroupsAdapter().apply {

                    vm.matchDetail.value?.let {
                        this.signupStartShow = it.signupStart
                        this.signupEndShow = it.signupEnd
                    }
                    setListener(this@MatchGroupsFragment)
                    matchGroupsAdapter = this
                }
                val spacing = resources.getDimension(R.dimen.dp_8).toInt()
                val top = resources.getDimension(R.dimen.dp_16).toInt()
                val linearItemDecoration = LinearItemDecoration(
                    bottom = top,
                    spacing = spacing,
                    isVertical = true
                )
                addItemDecoration(linearItemDecoration)
            }
            vm.matchDetail.observe(viewLifecycleOwner) {
                matchGroupsAdapter?.submitList(it.matchGroups)
            }
        }
    }

    override fun bindFragmentListener(context: Context) {
    }

    override fun getViewModel(): BaseViewModel? = null

    override fun onSignUpClick(data: MatchDetailDto.MatchGroup) {
        vm.matchDetail.value?.let {
            if (canSignUp(
                    signupStart = it.signupStart,
                    signupEnd = it.signupEnd
                )
            ) {
                requireContext().toMatchSignUp(data.id, data.matchId, data.token)
            } else {
                Alert.show(
                    requireContext(),
                    "警告",
                    getString(R.string.match_sign_up_stop)
                )
            }
        }
    }
}