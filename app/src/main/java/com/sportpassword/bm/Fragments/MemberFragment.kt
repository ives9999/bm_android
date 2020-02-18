package com.sportpassword.bm.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.login.LoginManager
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Adapters.MemberFunctionsAdapter
import com.sportpassword.bm.Controllers.*
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.Models.SuperCourses
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_member_functions.*
import kotlinx.android.synthetic.main.login_out.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.mask.text
import kotlinx.android.synthetic.main.member_function.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.tab_member.*

class MemberFragment: TabFragment() {

    val fixedRows: ArrayList<Map<String, String>> = arrayListOf(
            mapOf("text" to "帳戶資料", "icon" to "account", "segue" to "account"),
            mapOf("text" to "更改密碼", "icon" to "password", "segue" to "password")
    )
    var _rows: ArrayList<Map<String, String>> = arrayListOf()

    lateinit var memberFunctionsAdapter: MemberFunctionsAdapter
    protected lateinit var adapter: GroupAdapter<ViewHolder>
    protected val adapterSections: ArrayList<Section> = arrayListOf()
    var sections: ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataService = CourseService
        setHasOptionsMenu(true)
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
        _loginout()

    }

    override fun refresh() {
        if (member.isLoggedIn) {
            //initTeamList()
            mainActivity!!.refreshMember() { success ->
                if (success) {
                    _loginout()
                }
            }
        } else {
            _logoutBlock()
        }
    }


    private fun setValidateRow() {
        _rows.clear()
        for (row in fixedRows) {
            _rows.add(row)
        }
        if (member.isLoggedIn) {
            val validate: Int = member.validate
            if (validate and EMAIL_VALIDATE <= 0) {
                val function: Map<String, String> = mapOf("text" to "email認證", "icon" to "email", "segue" to "email")
                _rows.add(function)
            }
            if (validate and MOBILE_VALIDATE <= 0) {
                val function: Map<String, String> = mapOf("text" to "手機認證", "icon" to "mobile_validate", "segue" to "mobile")
                _rows.add(function)
            }
        }
    }
//    private fun setBlackListRow() {
//        if (member.isTeamManager) {
//            val row: Map<String, String> = mapOf("text" to "黑名單", "icon" to "blacklist", "segue" to "blacklist")
//            _rows.add(row)
//        }
//    }

    protected fun _loginout() {
        if (member.isLoggedIn) {
            _loginBlock()
        } else {
            _logoutBlock()
        }
    }
    protected fun _loginBlock() {
        _loginAdapter()
        nicknameLbl.text = member.nickname
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
        //menu_team_container.visibility = View.INVISIBLE
    }
    protected fun _loginAdapter() {

        adapter = GroupAdapter()
        adapter.setOnItemClickListener { item, view ->
            rowClick(item, view)
        }

        val items = generateItems()
        adapter.addAll(items)
        recyclerView.adapter = adapter
    }

    fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()

        setValidateRow()
        //setBlackListRow()
        val row: Map<String, String> = mapOf("text" to "重新整理", "icon" to "refresh", "segue" to "refresh")
        _rows.add(row)
        for (_row in _rows) {
            items.add(MemberItem(context!!, _row["text"]!!, _row["icon"]!!, _row["segue"]!!))
        }

        return items;
    }

    fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {

        val memberItem = item as MemberItem
        val segue = memberItem.segue

        when(segue) {
            "account" -> mainActivity!!.goEditMember()
            "password" -> goUpdatePassword()
            "email" -> mainActivity!!.goValidate("email")
            "mobile" -> mainActivity!!.goValidate("mobile")
//            "blacklist" -> goBlackList()
            "refresh" -> refresh()
        }
    }

    fun loginBtnPressed(view: View) {
        if (member.isLoggedIn) {
            if (member.uid.length > 0 && member.social == "fb") {
//                FacebookSdk.sdkInitialize(getApplicationContext());
//                AppEventsLogger.activateApp(this);
                LoginManager.getInstance().logOut()
            }
            MemberService.logout()
            refresh()
//            val memberDidChange = Intent(NOTIF_MEMBER_DID_CHANGE)
//            LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChange)
        } else {
            //goLogin()
            val loginIntent: Intent = Intent(activity, LoginActivity::class.java)
            //startActivity(loginIntent)
            startActivityForResult(loginIntent, mainActivity!!.LOGIN_REQUEST_CODE)

        }
    }

    fun registerBtnPressed(view: View){
        //goRegister()
        val registerIntent: Intent = Intent(activity, RegisterActivity::class.java)
        startActivityForResult(registerIntent, mainActivity!!.REGISTER_REQUEST_CODE)
    }

    fun forgetpasswordBtnPressed(view: View) {
        val forgetPasswordIntent = Intent(activity, ForgetPasswordActivity::class.java)
        startActivity(forgetPasswordIntent)
    }

    fun goUpdatePassword() {
        val updatePasswordIntent = Intent(activity, UpdatePasswordActivity::class.java)
        startActivity(updatePasswordIntent)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            mainActivity!!.LOGIN_REQUEST_CODE -> {
                _loginout()
            }
            mainActivity!!.REGISTER_REQUEST_CODE -> {
                _loginout()
            }
            mainActivity!!.VALIDATE_REQUEST_CODE -> {
                mainActivity!!.hideKeyboard()
                refresh()
            }
        }
    }


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

    class MemberItem(val context: Context, val text: String, val icon: String, val segue: String): Item() {
        override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

            viewHolder.text.text = text
            val iconID = context.resources.getIdentifier(icon, "drawable", context.packageName)
            viewHolder.icon.setImageResource(iconID)

        }

        override fun getLayout() = R.layout.adapter_member_functions

    }

}