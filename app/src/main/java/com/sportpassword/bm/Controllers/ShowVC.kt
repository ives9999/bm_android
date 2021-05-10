package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Models.CoursesTable
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.jsonToModel
import com.sportpassword.bm.member
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_show_course_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlin.reflect.KClass

open class ShowVC: BaseActivity() {

    var token: String? = null    // course token
    var tableRowKeys:MutableList<String> = mutableListOf()
    var tableRows: HashMap<String, HashMap<String,String>> = hashMapOf()

    var table: Table? = null

    lateinit var adapter: GroupAdapter<ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")
        }

        refreshLayout = refresh
        setRefreshListener()

        initAdapter()
        refresh()
    }

    open fun initAdapter() {
        adapter = GroupAdapter()
    }

    fun <T: Table> refresh1(t: KClass<T>){
        if (token != null) {
            Loading.show(mask)
            val params: HashMap<String, String> = hashMapOf("token" to token!!, "member_token" to member.token!!)
            dataService.getOne(this, params) { success ->
                if (success) {
                    val jsonString = dataService.jsonString
                    table = jsonToModel<t>(dataService.jsonString)

//                    if (superCourse != null) {
//                        superCourse!!.filter()
//                        //superCourse!!.print()
//                        superCoach = superCourse!!.coach
//                        setMainData()
//                        setSignupData()
//                        setCoachData()
//                        setFeatured()
//
//                        if (superCourse!!.isSignup) {
//                            signupButton.setText("取消報名")
//                        } else {
//                            val count = superCourse!!.signup_normal_models.count()
//                            if (count >= superCourse!!.people_limit) {
//                                signupButton.setText("候補")
//                            } else {
//                                signupButton.setText("報名")
//                            }
//                        }
//                    }
                }
                closeRefresh()
                Loading.hide(mask)
            }
        }
    }

}