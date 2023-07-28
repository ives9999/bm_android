package com.sportpassword.bm.bm_new.ui.match.manage_team_list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamListDto
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.base.ViewEvent
import com.sportpassword.bm.bm_new.ui.match.detail.MatchDetailActivity
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity
import com.sportpassword.bm.bm_new.ui.util.LinearItemDecoration
import com.sportpassword.bm.bm_new.ui.util.canSignUp
import com.sportpassword.bm.databinding.ActivityMatchBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.stateViewModel

//管理賽事
class MatchManageActivity : BaseActivity<ActivityMatchBinding>(),
    MatchManageListAdapter.Listener {

    private val vm by stateViewModel<MatchManageVM>()
    private var matchManageListAdapter: MatchManageListAdapter? = null
    private val editLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it: ActivityResult ->
            if (it.resultCode == Activity.RESULT_OK) {
                matchManageListAdapter?.refresh()
            }
        }


    override fun initViewBinding(): ActivityMatchBinding =
        ActivityMatchBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {}

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            tvName.text = getString(R.string.match_manage)

            btnClose.setOnClickListener { finish() }

            rv.run {
                layoutManager = LinearLayoutManager(this@MatchManageActivity)
                adapter = MatchManageListAdapter().apply {
                    setListener(this@MatchManageActivity)
                    matchManageListAdapter = this
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

            vm.getMatchTeamList().onEach {
                matchManageListAdapter?.submitData(it)
            }.launchIn(lifecycleScope)
        }
    }

    override fun getViewModel(): BaseViewModel = vm

    override fun handleViewEvent(event: ViewEvent) {
        super.handleViewEvent(event)
        when (event) {
            is MatchManageVM.MatchManageEvent.Delete -> {
                if (event.isSuccess) {
                    matchManageListAdapter?.refresh()
                    Toast.makeText(this, getString(R.string.match_manage_delete), Toast.LENGTH_LONG)
                        .show()
                }
            }

            else -> {}
        }
    }

    override fun onEditClick(data: MatchTeamListDto.Row) {
        if (canSignUp(
                signupStart = data.match.signupStart,
                signupEnd = data.match.signupEnd
            )
        ) {
            Intent(this, MatchSignUpActivity::class.java).apply {
                putExtra(MatchSignUpActivity.MATCH_TEAM_TOKEN, data.token)
                editLauncher.launch(this)
            }
        } else {
            Alert.show(
                this,
                "警告",
                getString(R.string.match_sign_up_stop_modify)
            )
        }
    }

    override fun onDelClick(data: MatchTeamListDto.Row) {
        vm.delMatchTeamList(data.token)
    }

    override fun onDetailClick(data: MatchTeamListDto.Row) {
        Intent(this, MatchDetailActivity::class.java).apply {
            putExtra(MatchDetailActivity.MATCH_TITLE, data.match.name)
            putExtra(MatchDetailActivity.MATCH_TOKEN, data.match.token)
            startActivity(this)
        }
    }
}