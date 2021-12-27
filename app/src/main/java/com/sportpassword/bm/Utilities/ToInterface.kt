package com.sportpassword.bm.Utilities

import android.content.Intent
import android.provider.MediaStore
import android.view.View
import com.sportpassword.bm.Controllers.*
import com.sportpassword.bm.member

interface ToInterface {

    var mainDelegate: BaseActivity

    fun manager(view: View) {
        toManager(view.tag as String)
    }

    fun setToDelegate(mainDelegate: BaseActivity) {
        mainDelegate.mainDelegate = mainDelegate
    }

    fun toAddCart(product_token: String? = null, cartItem_token: String? = null, delegate: BaseActivity?=null) {
        var msg: String = ""
        if (!member.isLoggedIn) {
            mainDelegate.warning("必須先登入會員，才能進行購買", true, "登入") {
                toLogin()
            }
        } else {
            msg = member.checkMust()

            if (msg.isNotEmpty()) {
                mainDelegate.warning(msg, true, "填寫") {
                    toRegister()
                }
            } else {

                if (delegate != null) {
                    mainDelegate.delegate = delegate
                }

                val i = Intent(mainDelegate, AddCartVC::class.java)
                i.putExtra("product_token", product_token)
                i.putExtra("cartItem_token", cartItem_token)
                mainDelegate.addCartVC.launch(i)


//                val i = Intent(mainDelegate, AddCartVC::class.java)
//                i.putExtra("product_token", product_token)
//                i.putExtra("cartItem_token", cartItem_token)
//                mainDelegate.startActivity(i)
            }
        }
    }
    fun toArena(member_like: Boolean = false, isPrevIconShow: Boolean = false, isSearchIconShow: Boolean = false) {
        val i = Intent(mainDelegate, ArenaVC::class.java)
        i.putExtra("member_like", member_like)
        i.putExtra("isPrevIconShow", isPrevIconShow)
        i.putExtra("isSearchIconShow", isSearchIconShow)

        mainDelegate.startActivity(i)
    }

    fun toBlackList() {
        val intent = Intent(mainDelegate, BlackListVC::class.java)
        mainDelegate.startActivity(intent)
    }

    fun toCoach(member_like: Boolean = false) {
        val i = Intent(mainDelegate, CoachVC::class.java)
        i.putExtra("member_like", member_like)
        mainDelegate.startActivity(i)
    }

    fun toCourse(member_like: Boolean, isPrevIconShow: Boolean = false, isSearchIconShow: Boolean = false) {
        val i = Intent(mainDelegate, CourseVC::class.java)
        i.putExtra("member_like", member_like)
        i.putExtra("isPrevIconShow", isPrevIconShow)
        i.putExtra("isSearchIconShow", isSearchIconShow)

        mainDelegate.startActivity(i)
    }

//    fun toEdit(source: String, title: String = "", token: String = "") {
//        val intent = Intent(mainDelegate, EditVC::class.java)
//        intent.putExtra("token", token)
//        intent.putExtra("source", source)
//        intent.putExtra("title", title)
//        mainDelegate.startActivity(intent)
//    }

    fun toEditContent(key: String, title: String, content: String, delegate: BaseActivity?=null) {

        val i = Intent(mainDelegate, ContentEditVC::class.java)
        i.putExtra("key", key)
        i.putExtra("title", title)
        i.putExtra("content", content)

        if (delegate != null) {
            mainDelegate.delegate = delegate
        }

        mainDelegate.editContentVC.launch(i)
    }

    fun toEditCourse(title: String, course_token: String, coach_token: String, delegate: BaseActivity?=null) {
        val i = Intent(mainDelegate, EditCourseVC::class.java)
        i.putExtra("title", title)
        i.putExtra("token", course_token)
        i.putExtra("coach_token", coach_token)

        mainDelegate.delegate = delegate
        mainDelegate.editCourseVC.launch(i)

        //mainDelegate.startActivityForResult(intent, GENERAL_REQUEST_CODE)
        //mainDelegate.editCourseResult?.launch(intent)
    }

    fun toEditTeam(token: String, delegate: BaseActivity?=null) {
        val i = Intent(mainDelegate, EditTeamVC::class.java)
        i.putExtra("token", token)

        mainDelegate.delegate = delegate
        mainDelegate.editTeamVC.launch(i)

        //mainDelegate.startActivityForResult(intent, GENERAL_REQUEST_CODE)
        //mainDelegate.editCourseResult?.launch(intent)
    }

//    fun toEditMember() {
//        val accountIntent = Intent(mainDelegate, AccountActivity::class.java)
//        mainDelegate.startActivity(accountIntent)
//    }

    fun toForgetPassword() {
        val forgetPasswordIntent: Intent = Intent(mainDelegate, ForgetPasswordVC::class.java)
        mainDelegate.startActivity(forgetPasswordIntent)
    }

    fun toLogin() {
        val i: Intent = Intent(mainDelegate, LoginVC::class.java)
        mainDelegate.loginVC.launch(i)
    }

    fun toManager(page: String) {
        if (!member.isLoggedIn) {
            Alert.show(mainDelegate, "警告", "請先登入會員")
            return
        }
        var intent: Intent? = null
        if (page == "team") {
            intent = Intent(mainDelegate, ManagerTeamVC::class.java)
            intent.putExtra("manager_token", member.token)
        } else if (page == "course") {
            intent = Intent(mainDelegate, ManagerCourseVC::class.java)
            intent.putExtra("manager_token", member.token)
        } else {
            intent = Intent(mainDelegate, ManagerVC::class.java)
            intent.putExtra("source", page)
        }
        mainDelegate.startActivity(intent)
    }

    fun toManagerFunction(title: String, token: String, source: String) {
        val intent = Intent(mainDelegate, ManagerFunctionVC1::class.java)
        intent.putExtra("title", title)
        intent.putExtra("token", token)
        intent.putExtra("source", source)
        mainDelegate.startActivity(intent)
    }

    fun toManagerSignup(able_type: String, able_token: String) {

        val intent = Intent(mainDelegate, ManagerSignupVC::class.java)
        intent.putExtra("able_type", able_type)
        intent.putExtra("able_token", able_token)
        mainDelegate.startActivity(intent)
    }

    fun toMember() {
        val i = Intent(mainDelegate, MemberVC::class.java)
        mainDelegate.startActivity(i)
    }

    fun toMemberCartList() {
        val i = Intent(mainDelegate, MemberCartListVC::class.java)
        mainDelegate.startActivity(i)
    }

    fun toMemberOrderList() {
        val i = Intent(mainDelegate, MemberOrderListVC::class.java)
        mainDelegate.startActivity(i)
    }

    fun toMemberSignupList(able_type: String) {
        val i = Intent(mainDelegate, MemberSignupListVC::class.java)
        i.putExtra("able_type", able_type)
        mainDelegate.startActivity(i)
    }

    fun toMore() {
        val i = Intent(mainDelegate, MoreVC::class.java)
        mainDelegate.startActivity(i)
    }

    fun toOrder() {
        var msg: String = ""
        if (!member.isLoggedIn) {
            mainDelegate.warning("必須先登入會員，才能進行購買", true, "登入") {
                toLogin()
            }
        } else {
            msg = member.checkMust()

            if (msg.isNotEmpty()) {
                mainDelegate.warning(msg, true, "填寫") {
                    toRegister()
                }
            } else {

                val i = Intent(mainDelegate, OrderVC::class.java)
                //i.putExtra("product_token", product_token)
                //i.putExtra("title", title)
                mainDelegate.startActivity(i)
            }
        }
    }


    fun toPayment(order_token: String, ecpay_token: String?=null, ecpay_token_ExpireDate: String?=null) {
        val i = Intent(mainDelegate, PaymentVC::class.java)
        i.putExtra("order_token", order_token)
        if (ecpay_token != null) {
            i.putExtra("ecpay_token", ecpay_token)
        }
        if (ecpay_token_ExpireDate != null) {
            i.putExtra("ecpay_token_ExpireDate", ecpay_token_ExpireDate)
        }
        mainDelegate.startActivity(i)
    }

    fun toProduct(member_like: Boolean = false) {
        val i = Intent(mainDelegate, ProductVC::class.java)
        i.putExtra("member_like", member_like)
        mainDelegate.startActivity(i)
    }

    fun toRegister() {
        val registerIntent: Intent = Intent(mainDelegate, RegisterVC::class.java)
        mainDelegate.startActivity(registerIntent)
    }

    fun toRequestManagerTeam() {
        val i = Intent(mainDelegate, RequestManagerTeamVC::class.java)
        mainDelegate.startActivity(i)
    }

    fun toSearch() {
        val i = Intent(mainDelegate, SearchVC::class.java)
        mainDelegate.startActivity(i)
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
        //mainDelegate.vcResult!!.selectCityResult(mainDelegate)
    }

    fun toSelectDate(key: String, selected: String?=null, delegate: BaseActivity?=null, able_type: String?=null) {
        val i = Intent(mainDelegate, DateSelectVC::class.java)

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

        mainDelegate.selectDateVC.launch(i)
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

    fun toSelectDeviceCamera() {

        val i = Intent("android.media.action.IMAGE_CAPTURE")
        if (i.resolveActivity(mainDelegate.packageManager) != null) {
            i.putExtra(MediaStore.EXTRA_OUTPUT, mainDelegate.fileUri)
            mainDelegate.selectDeviceCamera.launch(i)
        }
    }

    fun toSelectDevicePhoto() {

        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "image/*"
        mainDelegate.selectDevicePhoto.launch(i)
    }

    fun toSelectManager(manager_id: Int = 0, manager_token: String = "", able_type: String, delegate: BaseActivity) {

        val i = Intent(mainDelegate, SelectManagerVC::class.java)
        i.putExtra("manager_id", manager_id)
        i.putExtra("manager_token", manager_token)
        i.putExtra("able_type", able_type)

        mainDelegate.delegate = delegate
        mainDelegate.selectManager.launch(i)
    }

    fun <T> toSelectSingle(cls: Class<T>, key: String?=null, selected: String?=null, delegate: BaseActivity?=null, able_type: String?=null) {
        val i = Intent(mainDelegate, cls)

        if (able_type != null) {
            i.putExtra("able_type", able_type)
        }

        if (key != null) {
            i.putExtra("key", key)
        }

        if (selected != null) {
            i.putExtra("selected", selected)
        }

        if (delegate != null) {
            mainDelegate.delegate = delegate
        }

        mainDelegate.selectSingleVC.launch(i)

//        if (key == PRICE_UNIT_KEY) {
//            mainDelegate.selectPriceUnitVC.launch(i)
//        }
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

//    fun toSelectPriceUnit(selected: String?=null, delegate: BaseActivity?=null, able_type: String?=null) {
//        val i = Intent(mainDelegate, SelectPriceUnitVC::class.java)
//
//        if (able_type != null) {
//            i.putExtra("able_type", able_type)
//        }
//
//        if (delegate != null) {
//            mainDelegate.delegate = delegate
//        }
//
//        mainDelegate.selectPriceUnitVC.launch(i)
//    }

    fun toSelectWeekday(selected: String?=null, delegate: BaseActivity?=null, able_type: String?=null) {
        val i = Intent(mainDelegate, SelectWeekdayVC::class.java)

        if (able_type != null) {
            i.putExtra("able_type", able_type)
        }

        if (selected != null) {
            i.putExtra("selected", selected)
        }

        if (delegate != null) {
            mainDelegate.delegate = delegate
        }

        mainDelegate.selectWeekdayVC.launch(i)
    }

    fun toSelectWeekdays(selected: Int=0, delegate: BaseActivity?=null, able_type: String?=null) {

        val i = Intent(mainDelegate, SelectWeekdaysVC::class.java)

        if (able_type != null) {
            i.putExtra("able_type", able_type)
        }

        i.putExtra("selected1", selected)

        if (delegate != null) {
            mainDelegate.delegate = delegate
        }

        mainDelegate.selectWeekdaysVC.launch(i)
    }

    fun toShowArena(token: String) {
        val i = Intent(mainDelegate, ShowArenaVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toShowCoach(token: String) {
        val i = Intent(mainDelegate, ShowCoachVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toShowCourse(token: String) {
        val i = Intent(mainDelegate, ShowCourseVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toShowStore(token: String) {
        val i = Intent(mainDelegate, ShowStoreVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toShowProduct(token: String) {
        val i = Intent(mainDelegate, ShowProductVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toShowTeach(token: String) {
        val i = Intent(mainDelegate, ShowTeachVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toShowTeam(token: String) {
        val i = Intent(mainDelegate, ShowTeamVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toStore(member_like: Boolean = false) {
        val i = Intent(mainDelegate, StoreVC::class.java)
        i.putExtra("member_like", member_like)
        mainDelegate.startActivity(i)
    }

    fun toTeach(member_like: Boolean = false) {
        val i = Intent(mainDelegate, TeachVC::class.java)
        i.putExtra("member_like", member_like)
        mainDelegate.startActivity(i)
    }

    fun toTeam(params: HashMap<String, String>?, member_like: Boolean = false, isPrevIconShow: Boolean = false, isSearchIconShow: Boolean = false) {
        val i = Intent(mainDelegate, TeamVC::class.java)
        i.putExtra("member_like", member_like)
        i.putExtra("params", params)
        i.putExtra("isPrevIconShow", isPrevIconShow)
        i.putExtra("isSearchIconShow", isSearchIconShow)
        mainDelegate.startActivity(i)
    }

    fun toTeamTempPlayEdit(token: String) {
        val intent = Intent(mainDelegate, TeamTempPlayEditActivity::class.java)
        intent.putExtra("token", token)
        mainDelegate.startActivity(intent)
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

    fun toUpdatePassword() {
        val i = Intent(mainDelegate, UpdatePasswordVC::class.java)
        mainDelegate.updatePasswordVC.launch(i)
    }

    fun toValidate(type: String) {
//        val intent = Intent(mainDelegate, ValidateActivity::class.java)
//        intent.putExtra("type", type)
//        mainDelegate.startActivityForResult(intent, mainDelegate.VALIDATE_REQUEST_CODE)

        val i: Intent = Intent(mainDelegate, ValidateVC::class.java)
        i.putExtra("type", type)
        mainDelegate.validateVC.launch(i)

    }

    fun toYoutubePlayer(token: String) {
        val i = Intent(mainDelegate, YoutubePlayerVC::class.java)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }

    fun toTimeTable(source: String, token: String) {
        val i = Intent(mainDelegate, TimeTableVC::class.java)
        i.putExtra("source", source)
        i.putExtra("token", token)
        mainDelegate.startActivity(i)
    }
}