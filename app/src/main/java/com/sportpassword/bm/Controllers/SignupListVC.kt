package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.Models.SuperSignup
import com.sportpassword.bm.Models.SuperSignups
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.noSec
import com.sportpassword.bm.Utilities.noYear
import com.sportpassword.bm.member
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_signup_list_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.signup_list_cell.*
import org.json.JSONObject

class SignupListVC : MyTableVC() {

    var memberToken: String = ""
    var able: String = ""
    var able_token: String = ""//來源的token
    lateinit var signups: SuperSignups
    var able_model: SuperCourse = SuperCourse(JSONObject())
//    var signupRows: ArrayList<SuperSignup> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_list_vc)

        setMyTitle("報名列表")
        memberToken = member.token!!
        perPage = 15

        if (intent.hasExtra("able")) {
            able = intent.getStringExtra("able")
        }

        if (intent.hasExtra("able_token")) {
            able_token = intent.getStringExtra("able_token")
        }

        if (able == "course") {
            dataService = CourseService
        }
        recyclerView = list
        refreshLayout = list_refresh

        initAdapter()
        refresh()
    }

    override protected fun getDataStart(_page: Int, _perPage: Int) {
        //println("page: $_page")
        Loading.show(mask)
        dataService.signup_list(this, able_token, _page, _perPage) { success ->
            getDataEnd(success)
        }
    }

    override protected fun getDataEnd(success: Boolean) {
        if (success) {
            signups = dataService.superModel as SuperSignups

            if (page == 1) {
                able_model = dataService.able as SuperCourse
                setMyTitle(able_model.title + "報名列表")
                rows = arrayListOf()
                perPage = signups.perPage
                totalCount = signups.totalCount
                if (totalCount > 0) {
                    list_empty.visibility = View.INVISIBLE
                }
                var _pageCount: Int = totalCount / perPage
                totalPage = if (totalCount % perPage > 0) _pageCount+1 else _pageCount
            }
            rows.addAll(signups.rows)
            if (rows.size > 0) {
                notifyChanged()
//                var items = generateItems()
//                adapter.update(items)
//                adapter.notifyDataSetChanged()
            } else {
                list_empty.visibility = View.GONE
            }
            page = signups.page

            closeRefresh()

            page++
        }
        Loading.hide(mask)
        recyclerView.smoothScrollToPosition(scrollerPos + 1)
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        //if (page == 1) {
            val item = SignupItem(0, "報名者", "報名時間", "上課日期")
            items.add(item)
        //}
        for (i in 0..rows.size-1) {
            val row = rows[i] as SuperSignup
            //val no: Int = (page - 1) * perPage + i + 1
            val no: Int = i + 1
            items.add(SignupItem(no, row.member_name, row.created_at.noSec().noYear(), row.able_date))
        }

        return items
    }

}

class SignupItem(val no: Int, val signuper: String, val signupTime: String, val courseDate: String): Item() {

    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {
        var _no = no.toString()
        if (no == 0) {
            _no = "序號"
        }
        viewHolder.noLbl.setText(_no)
        viewHolder.signuperLbl.setText(signuper)
        viewHolder.signupTimeLbl.setText(signupTime)
        viewHolder.courseDateLbl.setText(courseDate)
    }

    override fun getLayout() = R.layout.signup_list_cell

}
