package com.sportpassword.bm.bm_new.ui.match

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.R
import com.sportpassword.bm.bm_new.data.dto.MatchListDto
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.util.LinearItemDecoration
import com.sportpassword.bm.databinding.ActivityMatchBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber

class MatchActivity : BaseActivity<ActivityMatchBinding>(), MatchListAdapter.Listener {

    private val vm by stateViewModel<MatchVM>()
    private var matchListAdapter: MatchListAdapter? = null

    override fun initViewBinding(): ActivityMatchBinding =
        ActivityMatchBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {}

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            btnClose.setOnClickListener { finish() }

            rv.run {
                layoutManager = LinearLayoutManager(this@MatchActivity)
                adapter = MatchListAdapter().apply {
                    setListener(this@MatchActivity)
                    matchListAdapter = this
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

            vm.getMatchList().onEach {
                matchListAdapter?.submitData(it)
            }.launchIn(lifecycleScope)
        }
    }

    override fun getViewModel(): BaseViewModel = vm

    override fun onDetailClick(data: MatchListDto.Row) {
        Timber.d("detail $data")
    }

    override fun onSignUpClick(data: MatchListDto.Row) {
        Timber.d("sign up $data")
    }
}