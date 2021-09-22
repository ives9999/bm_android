package com.sportpassword.bm.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Controllers.*
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.adapter_member_functions.*
import kotlinx.android.synthetic.main.login_out.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.mask.text
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.tab_member.*

class MemberFragment: TabFragment() {

    val fixedRows: ArrayList<Map<String, String>> = arrayListOf(
            mapOf("text" to "帳戶資料", "icon" to "account", "segue" to TO_PROFILE),
            mapOf("text" to "更改密碼", "icon" to "password", "segue" to TO_PASSWORD)
    )

    var memberRows: ArrayList<Map<String, String>> = arrayListOf()

    var orderRows: ArrayList<Map<String, String>> = arrayListOf(
        mapOf("text" to "購物車", "icon" to "cart", "segue" to TO_MEMBER_CART_LIST),
        mapOf("text" to "訂單查詢", "icon" to "order", "segue" to TO_MEMBER_ORDER_LIST)
    )
    var likeRows: ArrayList<Map<String, String>> = arrayListOf(
        mapOf("text" to "球隊", "icon" to "team", "segue" to "toLike", "able_type" to "team"),
        mapOf("text" to "球館", "icon" to "arena", "segue" to "toLike", "able_type" to "arena"),
        mapOf("text" to "教學", "icon" to "teach", "segue" to "toLike", "able_type" to "teach"),
        mapOf("text" to "教練", "icon" to "coach", "segue" to "toLike", "able_type" to "coach"),
        mapOf("text" to "課程", "icon" to "course", "segue" to "toLike", "able_type" to "course"),
        mapOf("text" to "商品", "icon" to "product", "segue" to "toLike", "able_type" to "product"),
        mapOf("text" to "體育用品店", "icon" to "store", "segue" to "toLike", "able_type" to "store")
    )

    var courseRows: ArrayList<Map<String, String>> = arrayListOf(
        mapOf("text" to "課程", "icon" to "course", "segue" to "manager_course")
    )

    var signupRows: ArrayList<Map<String, String>> = arrayListOf(
            mapOf("text" to "課程報名", "segue" to "calendar_course_signup")
    )

    var rows: ArrayList<ArrayList<String>> = arrayListOf()

    //lateinit var memberFunctionsAdapter: MemberFunctionsAdapter
    var mySections: ArrayList<HashMap<String, Any>> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataService = MemberService
        setHasOptionsMenu(true)

        //sections = arrayListOf("會員資料", "訂單", "喜歡", "課程")

        mySections = arrayListOf(
            hashMapOf("isExpanded" to true,"title" to "會員資料"),
            hashMapOf("isExpanded" to true,"title" to "訂單"),
            hashMapOf("isExpanded" to false,"title" to "喜歡"),
            hashMapOf("isExpanded" to true,"title" to "管理")
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val memuView = menu.findItem(R.id.menu_all).actionView
        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        searchBtn.visibility = View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_member, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = list_container
        refreshLayout = member_refresh
        maskView = mask

        val loginBtn = view.findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener { loginBtnPressed(view) }
        val reginBtn = view.findViewById<Button>(R.id.registerBtn)
        reginBtn.setOnClickListener { registerBtnPressed(view) }
        val forgetPasswordBtn = view.findViewById<Button>(R.id.forgetPasswordBtn)
        forgetPasswordBtn.setOnClickListener { forgetpasswordBtnPressed(view) }

        setRecyclerViewRefreshListener()
        loginout()
        //refresh()
    }

    override fun refresh() {
        if (member.isLoggedIn) {
            Loading.show(mask)
            dataService.getOne(mainActivity!!, hashMapOf("token" to member.token!!)) { success ->
                Loading.hide(mask)
                if (success) {
                    val table = jsonToModel<MemberTable>(MemberService.jsonString)
                    table?.toSession(mainActivity!!, true)
                    loginout()
                } else {
                    mainActivity!!.warning("無法從伺服器取得會員資料，請稍後再試或聯絡管理員")
                }
            }
//            mainActivity!!.refreshMember() { success ->
//                Loading.hide(mask)
//                if (success) {
//                    _loginout()
//                } else {
//                    mainActivity!!.warning("伺服器錯誤，請稍後再試或聯絡管理員")
//                }
//            }
        } else {
            _logoutBlock()
        }
    }


    private fun setValidateRow() {
        memberRows.clear()
        for (row in fixedRows) {
            memberRows.add(row)
        }
        if (member.isLoggedIn) {
            val validate: Int = member.validate
            if (validate and EMAIL_VALIDATE <= 0) {
                val function: Map<String, String> = mapOf("text" to "email認證", "icon" to "email", "segue" to "email")
                memberRows.add(function)
            }
            if (validate and MOBILE_VALIDATE <= 0) {
                val function: Map<String, String> = mapOf("text" to "手機認證", "icon" to "mobile_validate", "segue" to "mobile")
                memberRows.add(function)
            }
        }
    }
//    private fun setBlackListRow() {
//        if (member.isTeamManager) {
//            val row: Map<String, String> = mapOf("text" to "黑名單", "icon" to "blacklist", "segue" to "blacklist")
//            _rows.add(row)
//        }
//    }

    fun loginout() {
        //println(member.isLoggedIn)
        if (member.isLoggedIn) {
            _loginBlock()
        } else {
            _logoutBlock()
        }
    }
    protected fun _loginBlock() {
        _loginAdapter()
        nicknameLbl.text = member.nickname
        if (member.avatar!!.isNotEmpty()) {
            member.avatar!!.image(mainActivity!!, avatarView)
        }
        loginBtn.text = "登出"
        registerBtn.visibility = View.INVISIBLE
        forgetPasswordBtn.visibility = View.INVISIBLE
        member_container.visibility = View.VISIBLE
        //menu_team_container.visibility = View.VISIBLE
        refreshLayout = member_refresh
//        initMemberFunction()
    }
    protected fun _logoutBlock() {
        nicknameLbl.text = "未登入"
        loginBtn.text = "登入"
        registerBtn.visibility = View.VISIBLE
        forgetPasswordBtn.visibility = View.VISIBLE
        member_container.visibility = View.INVISIBLE
        avatarView.setImageResource(R.drawable.menuprofileicon)
        //menu_team_container.visibility = View.INVISIBLE
    }
    protected fun _loginAdapter() {

        adapter = GroupAdapter()
        adapter.setOnItemClickListener { item, view ->
            rowClick(item, view)
        }

        adapterSections.clear()
        for (section in mySections) {
            adapterSections.add(Section())
        }

        for ((idx, section) in mySections.withIndex()) {

            val title: String = section["title"] as String
            val isExpanded: Boolean = section["isExpanded"] as Boolean
            val expandableGroup = ExpandableGroup(GroupSection(title), isExpanded)

            val items = generateItems(idx)
            adapterSections[idx].addAll(items)
            expandableGroup.add(adapterSections[idx])
            adapter.add(expandableGroup)
        }

        recyclerView.adapter = adapter
    }

    override fun generateItems(section: Int): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()

        val _rows: ArrayList<Map<String, String>>
        if (section == 1) {
            _rows = orderRows
        } else if (section == 2) {
            _rows = likeRows
        } else if (section == 3) {
            _rows = courseRows
        } else {
            setValidateRow()
            //setBlackListRow()
            val row: Map<String, String> = mapOf("text" to "重新整理", "icon" to "refresh", "segue" to "refresh")
            memberRows.add(row)
            _rows = memberRows
        }

        for (_row in _rows) {
            //val text = if (_row.containsKey("text")) _row["text"]!! else ""
            //val icon = if (_row.containsKey("icon")) _row["icon"]!! else ""
            //val segue = if (_row.containsKey("segue")) _row["segue"]!! else ""
            items.add(MemberItem(requireContext(), _row))
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<GroupieViewHolder>, view: View) {

        val memberItem = item as MemberItem
        val row = memberItem.row
        var segue: String = ""
        if (row.containsKey("segue")) {
            segue = row["segue"]!!
        }

        when(segue) {
            TO_PROFILE -> mainActivity!!.toRegister()
            TO_PASSWORD -> toUpdatePassword()
            "email" -> mainActivity!!.toValidate("email")
            "mobile" -> mainActivity!!.toValidate("mobile")
//            "blacklist" -> goBlackList()
            "calendar_course_signup" -> toCalendarCourseSignup()
            "refresh" -> refresh()
            TO_MEMBER_ORDER_LIST -> mainActivity!!.toMemberOrderList()
            TO_MEMBER_CART_LIST -> mainActivity!!.toMemberCartList()
            "manager_course" -> mainActivity!!.toManager("course")
            TO_LIKE -> {
                var able_type: String? = null
                if (row.containsKey("able_type")) {
                    able_type = row["able_type"]
                }
                if (able_type != null) {
                    when(able_type) {
                        "team" -> mainActivity!!.toTeam(null, true)
                        "arena" -> mainActivity!!.toArena(true)
                        "teach" -> mainActivity!!.toTeach(true)
                        "coach" -> mainActivity!!.toCoach(true)
                        "course" -> mainActivity!!.toCourse(true)
                        "product" -> mainActivity!!.toProduct(true)
                        "store" -> mainActivity!!.toStore(true)
                    }
                }
            }
        }
    }

    fun loginBtnPressed(view: View) {
        if (member.isLoggedIn) {
            member.isLoggedIn = false
            member.reset()
            loginout()
            //MemberService.logout(mainActivity!!)
            //refresh()
        } else {
            mainActivity!!.toLogin()
        }
    }

    fun registerBtnPressed(view: View){
        mainActivity!!.toRegister()
//        val registerIntent: Intent = Intent(activity, RegisterActivity::class.java)
//        startActivityForResult(registerIntent, mainActivity!!.REGISTER_REQUEST_CODE)
    }
    fun forgetpasswordBtnPressed(view: View) {
        mainActivity!!.toForgetPassword()
//        val forgetPasswordIntent = Intent(activity, ForgetPasswordActivity::class.java)
//        startActivity(forgetPasswordIntent)
    }

    fun toUpdatePassword() {
        mainActivity!!.toUpdatePassword()
//        val updatePasswordIntent = Intent(activity, UpdatePasswordActivity::class.java)
//        startActivity(updatePasswordIntent)
    }

    fun toCalendarCourseSignup() {
        val intent = Intent(activity, CourseCalendarVC::class.java)
        startActivity(intent)
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            mainActivity!!.LOGIN_REQUEST_CODE -> {
//                loginout()
//            }
//            mainActivity!!.REGISTER_REQUEST_CODE -> {
//                loginout()
//            }
//            mainActivity!!.VALIDATE_REQUEST_CODE -> {
//                mainActivity!!.hideKeyboard()
//                refresh()
//            }
//        }
//    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isCourseShow = isVisibleToUser
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "TYPE"
        private val ARG_PARAM2 = "SCREEN_WIDTH"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TabFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: Int): TabFragment {
            val fragment = MemberFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    class MemberItem(val context: Context, val row: Map<String, String>): Item() {
        override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {

            if (row.containsKey("text")) {
                val text: String = row["text"]!!
                viewHolder.text.text = text
            }

            if (row.containsKey("icon")) {
                val icon: String = row["icon"]!!
                val iconID = context.resources.getIdentifier(icon, "drawable", context.packageName)
                viewHolder.icon.setImageResource(iconID)
            }

        }

        override fun getLayout() = R.layout.adapter_member_functions

    }

}