package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sportpassword.bm.Adapters.SignupsAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.databinding.ActivityShowTempPlayBinding
import com.sportpassword.bm.extensions.Alert
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import org.jetbrains.anko.contentView

class ShowTempPlayActivity : BaseActivity() {

    private lateinit var binding: ActivityShowTempPlayBinding
    private lateinit var view: ViewGroup

    lateinit var teamToken: String
    lateinit var signupsAdapter: SignupsAdapter
    lateinit var name: String
    lateinit var memberToken: String
    lateinit var nearDate: String

    lateinit var data: Map<String, Map<String, Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowTempPlayBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        teamToken = intent.getStringExtra(TOKEN_KEY)!!
        //println(token)

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.tempPlayShow_refresh)
        //println(refreshLayout)
        setRefreshListener()
        refresh()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.temp_play, menu)
//        return true
//    }

    override fun refresh() {
        super.refresh()
//        Loading.show(mask)
//        TeamService.getOne(this, "team", "name", teamToken) { success ->
//            if (success) {
//                data = TeamService.data
//                //println(data)
//                val name: String = data[NAME_KEY]!!["value"] as String
//                val id: Int = data[ID_KEY]!!["value"] as Int
//                setTeamData(featuredView)
//                signupsAdapter = SignupsAdapter(this, {token, near_date, status, off_at ->
//                    toTempPlaySignupOne(id, teamToken, name, near_date, token, status, off_at)
//                })
//                val layoutManager = LinearLayoutManager(this)
//                show_signups_container.adapter = signupsAdapter
//                show_signups_container.layoutManager = layoutManager
//                if (data.containsKey("signups") && data["signups"]!!.containsKey("value")) {
//                    val signups = data["signups"]!!["value"] as ArrayList<Map<String, String>>
//                    //println(signups)
//                    if (signups.size > 0) {
//                        signupsAdapter.lists = signups
//                        signupsAdapter.notifyDataSetChanged()
//                    }
//                }
//                closeRefresh()
//                setMyTitle(name)
//            }
//        }
        loadingAnimation.stop()
    }

    fun plusOne(view: View) {
        if (!member.isLoggedIn) {
            warning("請先登入會員")
            return
        }
        if ((member.validate and MOBILE_VALIDATE) == 0) {
            warning("要使用臨打功能，須先通過手機認證", true, "手機認證") {
                toValidate("mobile")
            }
            return
        }
        if (member.name!!.length == 0) {
            warning("要使用臨打功能，請先輸入真實姓名", true, "輸入姓名") {
                toRegister()
            }
            return
        }
        warning("報名臨打後，將會公開您的姓名與手機號碼給球隊管理員，方便球隊管理員跟您連絡\n是否真的要參加此球隊的臨打？", "取消報名", "確定臨打") {
            _plusOne()
        }
    }

    private fun _plusOne() {
        loadingAnimation.start()
        TeamService.plusOne(this, name, nearDate, memberToken) { success ->
            loadingAnimation.stop()
            val msg: String = "報名臨打成功"
            if (success) {
                Alert.show(this, "成功", msg)
                refresh()
            } else {
                if (!isFinishing) {
                    Alert.show(this@ShowTempPlayActivity, "警告", TeamService.msg)
                }
            }
        }
    }

    fun cancelPlusOne(view: View) {
        if (!member.isLoggedIn) {
            Alert.show(this, "警告", "請先登入會員")
            return
        }

        loadingAnimation.start()
        TeamService.cancelPlusOne(this, name, nearDate, memberToken) { success ->
            loadingAnimation.stop()
            var msg: String = "取消報名臨打成功"
            if (success) {
                Alert.show(this, "成功", msg)
                refresh()
            } else {
                Alert.show(this, "警告", TeamService.msg)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            VALIDATE_REQUEST_CODE -> {
//                _getMemberOne(member.token!!) {
//
//                }
//            }
//        }
    }

    override fun setTeamData(imageView: ImageView?) {
        if (imageView != null) {
            Picasso.get()
                    .load(data[FEATURED_KEY]!!["value"] as String)
                    .placeholder(R.drawable.loading_square)
                    .error(R.drawable.load_failed_square)
                    .into(imageView)
        }
        binding.showTempPlayCityBtn.text = data[CITY_KEY]!!["show"] as String
        binding.showTempPlayArenaBtn.text = data[ARENA_KEY]!!["show"] as String

        var key = ""
        var lbl = ""
        var text = ""
        key = TEAM_NEAR_DATE_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        nearDate = data[key]!!["value"] as String
        binding.showDate.text = "$lbl: $text"

        lbl = "臨打時段"
        text = data[TEAM_PLAY_START_KEY]!!["show"] as String + " - " + data[TEAM_PLAY_END_KEY]!!["show"] as String
        binding.showInterval.text = lbl + ": " + text

        key = TEAM_TEMP_QUANTITY_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        binding.showQuantity.text = lbl + ": " + text

        key = TEAM_TEMP_SIGNUP_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["value"] as String
        binding.showSignup.text = lbl + ": " + text

        key = TEAM_TEMP_FEE_M_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        binding.showFeeM.text = lbl + ": " + text

        key = TEAM_TEMP_FEE_F_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        binding.showFeeF.text = lbl + ": " + text

        key = TEAM_BALL_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        binding.showBall.text = lbl + ": " + text

        key = TEAM_LEADER_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        binding.showManager.text = lbl + ": " + text

        key = MOBILE_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        binding.showMobile.text = lbl + ": " + text

        key = TEAM_DEGREE_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        binding.showDegree.text = lbl + ": " + text

        name = data[NAME_KEY]!!["value"] as String
        memberToken = member.token!!
    }
}
