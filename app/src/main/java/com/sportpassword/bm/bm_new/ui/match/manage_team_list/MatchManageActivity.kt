package com.sportpassword.bm.bm_new.ui.match.manage_team_list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.R
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamListDto
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.base.ViewEvent
import com.sportpassword.bm.bm_new.ui.match.detail.MatchDetailActivity
import com.sportpassword.bm.bm_new.ui.util.LinearItemDecoration
import com.sportpassword.bm.databinding.ActivityMatchBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.stateViewModel

//管理賽事
class MatchManageActivity : BaseActivity<ActivityMatchBinding>(),
    MatchManageListAdapter.Listener {

    private val vm by stateViewModel<MatchManageVM>()
    private var matchManageListAdapter: MatchManageListAdapter? = null

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