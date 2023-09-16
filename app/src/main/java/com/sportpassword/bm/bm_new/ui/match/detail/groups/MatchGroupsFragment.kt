package com.sportpassword.bm.bm_new.ui.match.detail.groups

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.Controllers.LoginVC
import com.sportpassword.bm.Controllers.MemberVC
import com.sportpassword.bm.Controllers.ValidateVC
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
import com.sportpassword.bm.member
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
                matchGroupsAdapter?.apply {
                    signupStartShow = it.signupStart
                    signupEndShow = it.signupEnd
                    submitList(it.matchGroups)
                }
            }
        }
    }

    override fun bindFragmentListener(context: Context) {
    }

    override fun getViewModel(): BaseViewModel? = null

    override fun onSignUpClick(data: MatchDetailDto.MatchGroup) {

        //add by ives 2023/08/06 報名註冊前先檢查是否登入，如果沒有登入則導到登入頁
        //add by ives 2023/09/15 報名註冊前還要檢查會員是否通過email與手機認證
        if (!member.isLoggedIn) {
            toLogin()
            return
        }

        if (member.validate == 0) {
            warning("請先完成email與手機驗證", "取消", "驗證") {
                toValidate("email")
            }
            return
        }

        if (member.validate == 1) {
            warning("請先完成手機驗證", "取消", "驗證") {
                toValidate("mobile")
            }
            return
        }


        vm.matchDetail.value?.let {
            canSignUp(
                signupStart = it.signupStart,
                signupEnd = it.signupEnd,
                it.matchGroups.size >= data.limit
            )?.let { stringRes ->
                Alert.show(requireContext(), "警告", getString(stringRes))
            } ?: run {
                requireContext().toMatchSignUp(data.id, data.matchId, data.token)
            }
        }
    }

    fun toLogin() {
        val i: Intent = Intent(requireContext(), LoginVC::class.java)
        requireContext().startActivity(i)
    }

    fun toValidate(type: String) {

        val i: Intent = Intent(requireContext(), ValidateVC::class.java)
        i.putExtra("type", type)
        //validateVC.launch(i)
        requireContext().startActivity(i)
    }

//    val validateVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
//        if (res.resultCode == Activity.RESULT_OK) {
//
//            if (res.data != null) {
//                val i: Intent? = res.data
//
//                if (i != null) {
//                    refresh()
//                }
//            }
//        }
//    }

    fun warning(msg: String, closeButtonTitle: String, buttonTitle: String, buttonAction: () -> Unit) {
        Alert.show(requireContext(), "警告", msg, closeButtonTitle, buttonTitle, buttonAction)
    }
}