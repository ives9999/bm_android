package com.sportpassword.bm.bm_new.ui.match.team_list

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.R
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamListDto
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.util.LinearItemDecoration
import com.sportpassword.bm.databinding.ActivityMatchBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.stateViewModel

//管理賽事
class MatchTeamListActivity : BaseActivity<ActivityMatchBinding>(),
    MatchTeamListAdapter.Listener {

    private val vm by stateViewModel<MatchTeamListVM>()
    private var matchTeamListAdapter: MatchTeamListAdapter? = null

    override fun initViewBinding(): ActivityMatchBinding =
        ActivityMatchBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {}

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            tvName.text = getString(R.string.match_manage)

            btnClose.setOnClickListener { finish() }

            rv.run {
                layoutManager = LinearLayoutManager(this@MatchTeamListActivity)
                adapter = MatchTeamListAdapter().apply {
                    setListener(this@MatchTeamListActivity)
                    matchTeamListAdapter = this
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
                matchTeamListAdapter?.submitData(it)
            }.launchIn(lifecycleScope)
        }
    }

    override fun getViewModel(): BaseViewModel? = null

    override fun onDelClick(data: MatchTeamListDto.Row) {
        vm.delMatchTeamList(data.token)
    }

    override fun onDetailClick(data: MatchTeamListDto.Row) {
    }

    override fun onSignUpClick(data: MatchTeamListDto.Row) {
        //
    }
}