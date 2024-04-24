package com.sportpassword.bm.bm_new.ui.match.manage_team_list

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.Controllers.PaymentVC
import com.sportpassword.bm.Models.OrderTable
import com.sportpassword.bm.R
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamListDto
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.base.ViewEvent
import com.sportpassword.bm.bm_new.ui.match.detail.MatchDetailActivity
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity
import com.sportpassword.bm.bm_new.ui.util.LinearItemDecoration
import com.sportpassword.bm.bm_new.ui.util.canEditSignUp
import com.sportpassword.bm.databinding.ActivityMatchBinding
import com.sportpassword.bm.extensions.Alert
import com.sportpassword.bm.extensions.toDate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import java.text.SimpleDateFormat
import java.util.Date

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
        canEditSignUp(
            signupEnd = data.match.signupEnd
        )?.let { stringRes ->
            Alert.show(this, "警告", getString(stringRes))
        } ?: run {
            Intent(this, MatchSignUpActivity::class.java).apply {
                putExtra(MatchSignUpActivity.MATCH_TEAM_TOKEN, data.token)
                editLauncher.launch(this)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDelClick(data: MatchTeamListDto.Row) {
        val currentTime = Date()
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val stopTime = dateTimeFormat.parse(data.match.signupEnd)

        when {
            //已經報名且繳費，無法刪除隊伍
            data.orderTable?.process == "complete" -> {
                Alert.show(this, "警告", getString(R.string.match_manage_cant_delete_paid))
            }
            //已經超過報名截止時間，無法刪除隊伍
            currentTime.after(stopTime) -> {
                Alert.show(this, "警告", getString(R.string.match_manage_cant_delete_expired))
            }

            else -> {
                vm.delMatchTeamList(data.token)
            }
        }
    }

    override fun onDetailClick(data: MatchTeamListDto.Row) {
        Intent(this, MatchDetailActivity::class.java).apply {
            putExtra(MatchDetailActivity.MATCH_TITLE, data.match.name)
            putExtra(MatchDetailActivity.MATCH_TOKEN, data.match.token)
            startActivity(this)
        }
    }

    override fun onPaymentClick(data: MatchTeamListDto.Row) {
        //println(data)
        val matchTable = data.match
        val signupStart: String = matchTable.signupStart
        val signupEnd: String = matchTable.signupEnd
        val order_id: Int? = data.orderId
        val orderTable: OrderTable? = data.orderTable

        if (order_id != null) {
            data.orderTable?.let { toPayment(it.token, null, null, "match") }
        } else {
            var isInterval: Boolean = false
            val signupStartDate: Date? = signupStart.toDate()
            val signupEndDate: Date? = signupEnd.toDate()
        }
    }

    fun toPayment(
        order_token: String,
        ecpay_token: String? = null,
        ecpay_token_ExpireDate: String? = null,
        source: String = "order"
    ) {
        //mainDelegate.finish()
        val i = Intent(this, PaymentVC::class.java)
        i.putExtra("order_token", order_token)
        i.putExtra("source", source)
        if (ecpay_token != null) {
            i.putExtra("ecpay_token", ecpay_token)
        }
        if (ecpay_token_ExpireDate != null) {
            i.putExtra("ecpay_token_ExpireDate", ecpay_token_ExpireDate)
        }
        this.startActivity(i)
    }
}