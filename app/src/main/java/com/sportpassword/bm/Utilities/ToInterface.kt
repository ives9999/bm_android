package com.sportpassword.bm.Utilities

import android.content.Intent
import android.view.View
import com.sportpassword.bm.Controllers.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.mask.*

interface ToInterface {

    var mainDelegate: BaseActivity

    fun setToDelegate(mainDelegate: BaseActivity) {
        mainDelegate.mainDelegate = mainDelegate
    }

    fun toLogin() {
        val loginIntent: Intent = Intent(mainDelegate, LoginActivity::class.java)
        mainDelegate.startActivity(loginIntent)
    }

    fun toRegister() {
        val registerIntent: Intent = Intent(mainDelegate, RegisterActivity::class.java)
        mainDelegate.startActivity(registerIntent)
    }

    fun toForgetPassword() {
        val forgetPasswordIntent: Intent = Intent(mainDelegate, ForgetPasswordActivity::class.java)
        mainDelegate.startActivity(forgetPasswordIntent)
    }

    fun toEdit(source: String, title: String = "", token: String = "") {
        val intent = Intent(mainDelegate, EditVC1::class.java)
        intent.putExtra("token", token)
        intent.putExtra("source", source)
        intent.putExtra("title", title)
        mainDelegate.startActivity(intent)
    }

    fun toTeamTempPlayEdit(token: String) {
        val intent = Intent(mainDelegate, TeamTempPlayEditActivity::class.java)
        intent.putExtra("token", token)
        mainDelegate.startActivity(intent)
    }

    fun manager(view: View) {
        toManager(view.tag as String)
    }

    fun toManager(page: String) {
        if (!member.isLoggedIn) {
            Alert.show(mainDelegate, "警告", "請先登入會員")
            return
        }
        var intent: Intent? = null
        if (page == "course") {
            intent = Intent(mainDelegate, ManagerCourseVC::class.java)
            intent.putExtra("manager_token", member.token)
        } else {
            intent = Intent(mainDelegate, ManagerVC::class.java)
            intent.putExtra("source", page)
        }
        if (intent != null) {
            mainDelegate.startActivity(intent)
        }
    }
    fun toManagerFunction(title: String, token: String, source: String) {
        val intent = Intent(mainDelegate, ManagerFunctionVC1::class.java)
        intent.putExtra("title", title)
        intent.putExtra("token", token)
        intent.putExtra("source", source)
        mainDelegate.startActivity(intent)
    }

    fun toEditCourse(title: String, course_token: String, coach_token: String) {
        val intent = Intent(mainDelegate, EditCourseVC::class.java)
        intent.putExtra("title", title)
        intent.putExtra("course_token", course_token)
        intent.putExtra("coach_token", coach_token)

        mainDelegate.startActivityForResult(intent, GENERAL_REQUEST_CODE)
    }

    fun toTempPlayDate(name: String, token: String) {
        val intent = Intent(mainDelegate, TempPlayDateVC::class.java)
        intent.putExtra("name", name)
        intent.putExtra("token", token)
        mainDelegate.startActivity(intent)
    }
    fun toTempPlayDatePlayer(date: String, name: String, token: String) {
        val intent = Intent(mainDelegate, TempPlayDatePlayerVC::class.java)
        intent.putExtra("date", date)
        intent.putExtra("teamName", name)
        intent.putExtra("token", token)
        mainDelegate.startActivity(intent)
    }

    fun toSearch(type: String) {
        //val intent = Intent(mainDelegate)
    }

//    fun toDelete(page: String, token: String = "") {
//        Alert.delete(mainDelegate, {
//            Loading.show(mask)
//            dataService.delete(mainDelegate, page, token) { success ->
//                Loading.hide(mask)
////                val teamUpdate = Intent(NOTIF_TEAM_UPDATE)
////                LocalBroadcastManager.getInstance(mainDelegate).sendBroadcast(teamUpdate)
//                toManager(page)
//            }
//        })
//    }
//
//    fun toDelete1(page: String, token: String = "") {
//        Alert.delete(mainDelegate, {
//            Loading.show(mask)
//            dataService.delete(mainDelegate, page, token) { success ->
//                Loading.hide(mask)
//                refresh()
//            }
//        })
//    }

    fun toEditMember() {
        val accountIntent = Intent(mainDelegate, AccountActivity::class.java)
        mainDelegate.startActivity(accountIntent)
    }

    fun toValidate(type: String) {
        val intent = Intent(mainDelegate, ValidateActivity::class.java)
        intent.putExtra("type", type)
        mainDelegate.startActivityForResult(intent, mainDelegate.VALIDATE_REQUEST_CODE)
    }

    fun toTempPlaySignupOne(teamId: Int, teamToken: String, teamName: String, near_date: String, memberToken: String, status: String, off_at: String) {
        val i = Intent(mainDelegate, TempPlaySignupOneVC::class.java)
        i.putExtra("id", teamId)
        i.putExtra("name", teamName)
        i.putExtra("token", teamToken)
        i.putExtra("near_date", near_date)
        i.putExtra("memberToken", memberToken)
        i.putExtra("status", status)
        i.putExtra("off_at", off_at)
        mainDelegate.startActivity(i)
    }

    fun toBlackList() {
        val intent = Intent(mainDelegate, BlackListVC::class.java)
        mainDelegate.startActivity(intent)
    }

    fun toCourse(member_like: Boolean) {
        val i = Intent(mainDelegate, CourseVC::class.java)
        i.putExtra("member_like", member_like)
        mainDelegate.startActivity(i)
    }

    fun toTeam(params: HashMap<String, Any>?, member_like: Boolean = false) {
        val i = Intent(mainDelegate, TeamVC::class.java)
        i.putExtra("member_like", member_like)
        i.putExtra("params", params)
        mainDelegate.startActivity(i)
    }

    fun toCoach(member_like: Boolean = false) {
        val i = Intent(mainDelegate, CoachVC::class.java)
        i.putExtra("member_like", member_like)
        mainDelegate.startActivity(i)
    }

    fun toArena(member_like: Boolean = false) {
        val i = Intent(mainDelegate, ArenaVC::class.java)
        i.putExtra("member_like", member_like)
        mainDelegate.startActivity(i)
    }

    fun toTeach() {
        val i = Intent(mainDelegate, TeachVC::class.java)
        i.putExtra("type", "teach")
        i.putExtra("titleField", "title")
        mainDelegate.startActivity(i)
    }

    fun toStore(member_like: Boolean = false) {
        val i = Intent(mainDelegate, StoreVC::class.java)
        i.putExtra("member_like", member_like)
        mainDelegate.startActivity(i)
    }

    fun toProduct(member_like: Boolean = false) {
        val i = Intent(mainDelegate, ProductVC::class.java)
        i.putExtra("member_like", member_like)
        mainDelegate.startActivity(i)
    }

    fun toShowCourse(token: String) {
        val i = Intent(mainDelegate, ShowCourseVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toShowTeam(token: String) {
        val i = Intent(mainDelegate, ShowTeamVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toShowCoach(token: String) {
        val i = Intent(mainDelegate, ShowCoachVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toShowArena(token: String) {
        val i = Intent(mainDelegate, ShowArenaVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toShowProduct(token: String) {
        val i = Intent(mainDelegate, ShowProductVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toShowStore(token: String) {
        val i = Intent(mainDelegate, ShowStoreVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toOrder(product_token: String) {
        var msg: String = ""
        if (!member.isLoggedIn) {
            mainDelegate.warning("必須先登入會員，才能進行購買", true, "登入") {
                toLogin()
            }
        } else {
            //val _member: Member = Member(JSONObject())
            for (key in MEMBER_MUST_ARRAY) {
                val type: String = MEMBER_ARRAY[key]!!["type"]!!
                val value: String = member.fetch(key)
                if (value.isEmpty() || value == "0") {
                    msg += MEMBER_MUST_ARRAY_WARNING[key]!! + "\n"
                }
            }

            if (msg.isNotEmpty()) {
                mainDelegate.warning(msg, true, "填寫") {
                    toRegister()
                }
            } else {

                val i = Intent(mainDelegate, OrderVC::class.java)
                i.putExtra("product_token", product_token)
                //i.putExtra("title", title)
                mainDelegate.startActivity(i)
            }
        }
    }

    fun toPayment(order_token: String, ecpay_token: String?=null, tokenExpireDate: String?=null) {
        val i = Intent(mainDelegate, PaymentVC::class.java)
        i.putExtra("order_token", order_token)
        if (ecpay_token != null) {
            i.putExtra("ecpay_token", ecpay_token)
        }
        if (tokenExpireDate != null) {
            i.putExtra("tokenExpireDate", tokenExpireDate)
        }
        mainDelegate.startActivity(i)
    }


    fun toTimeTable(source: String, token: String) {
        val i = Intent(mainDelegate, TimeTableVC::class.java)
        i.putExtra("source", source)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toMemberOrderList() {
        val i = Intent(mainDelegate, MemberOrderListVC::class.java)
        mainDelegate.startActivity(i)
    }

    fun toSelectCity(selected: String?=null, delegate: BaseActivity?=null, able_type: String?=null) {

        val i = Intent(mainDelegate, SelectCityVC::class.java)

        i.putExtra("selected", selected)

        if (able_type != null) {
            i.putExtra("able_type", able_type)
        }

        if (delegate != null) {
            mainDelegate.delegate = delegate
        }

        mainDelegate.selectCityVC.launch(i)
    }

    fun toSelectArea(selected: String?=null, city_id: Int, delegate: BaseActivity?=null, able_type: String?=null) {

        val i = Intent(mainDelegate, SelectAreaVC::class.java)

        i.putExtra("selected", selected)

        if (able_type != null) {
            i.putExtra("able_type", able_type)
        }

        if (delegate != null) {
            mainDelegate.delegate = delegate
        }

        i.putExtra("city_id", city_id)

        mainDelegate.selectAreaVC.launch(i)
    }

    fun toSelectWeekday(selected: String?=null, delegate: BaseActivity?=null, able_type: String?=null) {
        val i = Intent(mainDelegate, SelectWeekdayVC::class.java)

        if (able_type != null) {
            i.putExtra("able_type", able_type)
        }

        if (delegate != null) {
            mainDelegate.delegate = delegate
        }

        mainDelegate.selectWeekdayVC.launch(i)
    }

    fun toSelectTime(key: String, selected: String?=null, delegate: BaseActivity?=null, able_type: String?=null) {
        val i = Intent(mainDelegate, SelectTimeVC::class.java)

        i.putExtra("key", key)
        if (selected != null) {
            i.putExtra("selected", selected)
        }

        if (able_type != null) {
            i.putExtra("able_type", able_type)
        }

        if (delegate != null) {
            mainDelegate.delegate = delegate
        }

        mainDelegate.selectTimeVC.launch(i)
    }

    fun toSelectArena(selected: String?=null, city_id: Int, delegate: BaseActivity?=null, able_type: String?=null) {

        val i = Intent(mainDelegate, SelectArenaVC::class.java)

        if (selected != null) {
            i.putExtra("selected", selected)
        }

        if (able_type != null) {
            i.putExtra("able_type", able_type)
        }

        if (delegate != null) {
            mainDelegate.delegate = delegate
        }

        i.putExtra("city_id", city_id)

        mainDelegate.selectArenaVC.launch(i)
    }

    fun toSelectDegree(selected: String?=null, delegate: BaseActivity?=null, able_type: String?=null) {

        val i = Intent(mainDelegate, SelectDegreeVC::class.java)

        if (able_type != null) {
            i.putExtra("able_type", able_type)
        }

        if (selected != null && selected.isNotEmpty()) {
            val selecteds: ArrayList<String> = selected.split(",").toCollection(ArrayList())
            i.putStringArrayListExtra("selecteds", selecteds)
        }

        if (delegate != null) {
            mainDelegate.delegate = delegate
        }

        mainDelegate.selectDegreeVC.launch(i)
    }
}