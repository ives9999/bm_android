package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.databinding.ActivitySignupListVcBinding
import com.sportpassword.bm.member

class SignupListVC : MyTableVC() {

    private lateinit var binding: ActivitySignupListVcBinding
    //private lateinit var view: ViewGroup

    var memberToken: String = ""
    var able: String = ""
    var able_token: String = ""//來源的token
    var able_model: CourseTable = CourseTable()
//    var signupRows: ArrayList<SuperSignup> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupListVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        setMyTitle("報名列表")
        memberToken = member.token!!
        perPage = 15

        if (intent.hasExtra("able")) {
            able = intent.getStringExtra("able")!!
        }

        if (intent.hasExtra("able_token")) {
            able_token = intent.getStringExtra("able_token")!!
        }

        if (able == "course") {
            dataService = CourseService
        }
        recyclerView = binding.list
        refreshLayout = binding.listRefresh

        //initAdapter()
        refresh()
    }

    override fun getDataStart(_page: Int, _perPage: Int, token: String?) {
        //println("page: $_page")
        Loading.show(view)
//        dataService.signup_list(this, able_token, _page, _perPage) { success ->
//            getDataEnd(success)
//        }
    }

    override fun getDataEnd(success: Boolean) {
        if (success) {
            //signups = dataService.superModel as SuperSignups

            if (page == 1) {
                able_model = dataService.able as CourseTable
                setMyTitle(able_model.title + "報名列表")
                //rows = arrayListOf()
                //perPage = signups.perPage
                //totalCount = signups.totalCount
                if (totalCount > 0) {
                    binding.listEmpty.visibility = View.INVISIBLE
                }
                var _pageCount: Int = totalCount / perPage
                totalPage = if (totalCount % perPage > 0) _pageCount+1 else _pageCount
            }
            //rows.addAll(signups.rows)
//            if (rows.size > 0) {
                //notifyChanged()
//                var items = generateItems()
//                adapter.update(items)
//                adapter.notifyDataSetChanged()
//            } else {
//                list_empty.visibility = View.GONE
//            }
            //page = signups.page

            closeRefresh()

            page++
        }
        Loading.hide(view)
        //recyclerView.smoothScrollToPosition(scrollerPos + 1)
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

//    override fun generateItems(): ArrayList<Item> {
//        val items: ArrayList<Item> = arrayListOf()
//        //if (page == 1) {
//            val item = SignupItem(0, "報名者", "報名時間", "上課日期")
//            items.add(item)
//        //}
////        for (i in 0..rows.size-1) {
////            val row = rows[i] as SuperSignup
////            //val no: Int = (page - 1) * perPage + i + 1
////            val no: Int = i + 1
////            items.add(SignupItem(no, row.member_name, row.created_at.noSec().noYear(), row.able_date))
////        }
//
//        return items
//    }

}

//class SignupItem(val no: Int, val signuper: String, val signupTime: String, val courseDate: String): Item() {
//
//    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
//        var _no = no.toString()
//        if (no == 0) {
//            _no = "序號"
//        }
//        viewHolder.noLbl.setText(_no)
//        viewHolder.signuperLbl.setText(signuper)
//        viewHolder.signupDateLbl.setText(signupTime)
//        viewHolder.courseDateLbl.setText(courseDate)
//    }
//
//    override fun getLayout() = R.layout.signup_list_cell
//
//}
