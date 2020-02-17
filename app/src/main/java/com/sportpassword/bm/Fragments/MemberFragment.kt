package com.sportpassword.bm.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.login.LoginManager
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Adapters.MemberFunctionsAdapter
import com.sportpassword.bm.Controllers.ForgetPasswordActivity
import com.sportpassword.bm.Controllers.LoginActivity
import com.sportpassword.bm.Controllers.RegisterActivity
import com.sportpassword.bm.Controllers.ShowCourseVC
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
import kotlinx.android.synthetic.main.login_out.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.menu_member_function.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.tab_member.*

class MemberFragment: TabFragment() {

    val fixedRows: ArrayList<Map<String, String>> = arrayListOf(
            mapOf("text" to "帳戶資料", "icon" to "account", "segue" to "account"),
            mapOf("text" to "更改密碼", "icon" to "password", "segue" to "password")
    )
    var _rows: ArrayList<Map<String, String>> = arrayListOf()

    lateinit var memberFunctionsAdapter: MemberFunctionsAdapter


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
        //recyclerView = list_container
        refreshLayout = member_refresh
        maskView = mask

//        recyclerView.setHasFixedSize(true)
//        setRecyclerViewScrollListener()
//        setRecyclerViewRefreshListener()
        _loginout()
        //refresh()

    }

    override fun refresh() {
        if (member.isLoggedIn) {
            //initTeamList()
//            refreshMember() { success ->
//                closeRefresh()
//                if (success) {
//                    _loginout()
//                }
//            }
        } else {
            //_logoutBlock()
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
    protected fun _goMemberFunctions(segue: String) {
        when(segue) {
//            "account" -> goEditMember()
//            "password" -> goUpdatePassword()
//            "email" -> goValidate("email")
//            "mobile" -> goValidate("mobile")
//            "blacklist" -> goBlackList()
//            "refresh" -> goRefresh()
        }
    }
    protected fun _loginAdapter() {
        setValidateRow()
        //setBlackListRow()
        val row: Map<String, String> = mapOf("text" to "重新整理", "icon" to "refresh", "segue" to "refresh")
        _rows.add(row)
        memberFunctionsAdapter = MemberFunctionsAdapter(context!!, _rows, {
            type -> _goMemberFunctions(type)
        })
        member_functions_container.adapter = memberFunctionsAdapter
        val layoutManager = LinearLayoutManager(activity)
        member_functions_container.layoutManager = layoutManager
        memberFunctionsAdapter.notifyDataSetChanged()
    }

    protected fun goRefresh() {
        mainActivity!!._getMemberOne(member.token) {
            _loginout()
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
                goRefresh()
            }
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

}