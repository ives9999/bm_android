package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.sportpassword.bm.Adapters.SignupsAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_temp_play.*
import org.jetbrains.anko.contentView

class ShowTempPlayActivity : BaseActivity() {

    lateinit var teamToken: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_temp_play)

        teamToken = intent.getStringExtra(TEAM_TOKEN_KEY)
        //println(token)

        signupsAdapter = SignupsAdapter(this)
        val layoutManager = LinearLayoutManager(this)
        show_signups_container.adapter = signupsAdapter
        show_signups_container.layoutManager = layoutManager

        refreshLayout = contentView!!.findViewById< SwipeRefreshLayout>(R.id.tempPlayShow_refresh)
        //println(refreshLayout)
        setRefreshListener()
        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.temp_play, menu)
        return true
    }

    override fun refresh() {
        super.refresh()
        TeamService.getOne(this, "team", "name", teamToken) { success ->
            if (success) {
                data = TeamService.data
                //println(data)
                setTeamData(show_featured_view)
                closeRefresh()
                val title: String = data[TEAM_NAME_KEY]!!["value"] as String
                setMyTitle(title)
            }
        }
    }

    fun plusOne(view: View) {
        if (!member.isLoggedIn) {
            Alert.show(this, "警告", "請先登入會員")
            return
        }

        TeamService.plusOne(this, name, nearDate, memberToken) { success ->
            var msg: String = "報名臨打成功"
            if (success) {
                Alert.show(this, "成功", msg)
                refresh()
            } else {
                Alert.show(this, "警告", TeamService.msg)
            }
        }
    }

    fun cancelPlusOne(view: View) {
        if (!member.isLoggedIn) {
            Alert.show(this, "警告", "請先登入會員")
            return
        }

        TeamService.cancelPlusOne(this, name, nearDate, memberToken) { success ->
            var msg: String = "取消報名臨打成功"
            if (success) {
                Alert.show(this, "成功", msg)
                refresh()
            } else {
                Alert.show(this, "警告", TeamService.msg)
            }
        }
    }

    override fun setTeamData(imageView: ImageView?) {
        if (imageView != null) {
            Picasso.with(this)
                    .load(data[TEAM_FEATURED_KEY]!!["value"] as String)
                    .placeholder(R.drawable.loading_square)
                    .error(R.drawable.load_failed_square)
                    .into(imageView)
        }
        show_temp_play_city_btn.text = data[TEAM_CITY_KEY]!!["show"] as String
        show_temp_play_arena_btn.text = data[TEAM_ARENA_KEY]!!["show"] as String

        var key = ""
        var lbl = ""
        var text = ""
        key = TEAM_NEAR_DATE_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        nearDate = data[key]!!["value"] as String
        show_date.text = "$lbl: $text"

        lbl = "臨打時段"
        text = data[TEAM_PLAY_START_KEY]!!["show"] as String + " - " + data[TEAM_PLAY_END_KEY]!!["show"] as String
        show_interval.text = lbl + ": " + text

        key = TEAM_TEMP_QUANTITY_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        show_quantity.text = lbl + ": " + text

        key = TEAM_TEMP_SIGNUP_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["value"] as String
        show_signup.text = lbl + ": " + text

        key = TEAM_TEMP_FEE_M_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        show_fee_M.text = lbl + ": " + text

        key = TEAM_TEMP_FEE_F_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        show_fee_Ｆ.text = lbl + ": " + text

        key = TEAM_BALL_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        show_ball.text = lbl + ": " + text

        key = TEAM_LEADER_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        show_manager.text = lbl + ": " + text

        key = TEAM_MOBILE_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        show_mobile.text = lbl + ": " + text

        key = TEAM_DEGREE_KEY
        lbl = data[key]!!["ch"] as String
        text = data[key]!!["show"] as String
        show_degree.text = lbl + ": " + text

        if (data.containsKey("signups") && data["signups"]!!.containsKey("value")) {
            val signups = data["signups"]!!["value"] as ArrayList<Map<String, String>>
            println(signups)
            if (signups.size > 0) {
                signupsAdapter.lists = signups
                signupsAdapter.notifyDataSetChanged()
            }
        }

        name = data[TEAM_NAME_KEY]!!["value"] as String
        memberToken = member.token
    }
}
