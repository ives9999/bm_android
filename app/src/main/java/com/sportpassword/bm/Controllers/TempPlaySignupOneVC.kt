package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.sportpassword.bm.Adapters.TempPlaySignupOneAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_temp_play_signup_one_vc.*
import kotlinx.android.synthetic.main.mask.*
import org.jetbrains.anko.contentView

class  TempPlaySignupOneVC : BaseActivity() {

    var team_name: String = ""
    var teamToken: String = ""
    var team_id: Int = -1
    var near_date: String = ""

    var isTeamManager: Boolean = false
    var memberName: String = ""
    var memberToken: String = ""
    var memberMobile: String = ""
    var memberOne: ArrayList<HashMap<String, HashMap<String, Any>>> = arrayListOf()
    var keys: ArrayList<String> = arrayListOf("team", NAME_KEY, MOBILE_KEY, "black_list")

    lateinit var tempPlaySignupOneAdapter: TempPlaySignupOneAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_play_signup_one_vc)

        memberToken = intent.getStringExtra("memberToken")
        teamToken = intent.getStringExtra("token")
        team_name = intent.getStringExtra("name")
        team_id = intent.getIntExtra("id", -1)
        near_date = intent.getStringExtra("near_date")
//        println(memberToken)
//        println(team_name)
//        println(team_id)
//        println(near_date)
        tempPlaySignupOneAdapter = TempPlaySignupOneAdapter(this, { itemKey, mobile ->
            //println(itemKey)
            if (itemKey == MOBILE_KEY) {
                //println(isTeamManager)
                if (!isTeamManager) {
                    warning("您不是此球隊管理員，所以無法檢視並撥打球友的電話")
                } else {
                    memberMobile = mobile
                    info("球友電話是："+mobile,"取消","撥打電話", {
                        myMakeCall(memberMobile)
                    })
                }
            } else if (itemKey == "black_list") {
//                val row = getRow(itemKey)
//                println(row)
                addBlackList(memberName, memberToken, teamToken)
            }
        })
        tempPlaySignupOneAdapter.lists = memberOne
        tempPlaySignupOneAdapter.keys = keys
        data_list.adapter = tempPlaySignupOneAdapter
        val layoutManager = LinearLayoutManager(this)
        data_list.layoutManager = layoutManager

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.tempPlaySignupOne_refresh)
        setRefreshListener()

        refresh()
    }

    override fun refresh() {
        super.refresh()
        initMemberOne()
        Loading.show(mask)
        initGetMemberValue { success ->
            if (success) {
                initIsTeamManager { success ->
                    Loading.hide(mask)
                    tempPlaySignupOneAdapter.notifyDataSetChanged()
                    closeRefresh()
                }
            } else {
//                loading.dismiss()
            }
        }
    }

    fun initGetMemberValue(completion: CompletionHandler) {
        _getMemberOne(memberToken) { success ->
            if (success) {
                val one = MemberService.one
                if (one == null) {
                    warning("無法取得該會員資訊，請稍後再試")
                } else {
                    for ((idx, key) in keys.withIndex()) {
                        val keys1: Iterator<String> = one.keys()
                        while (keys1.hasNext()) {
                            val key1: String = keys1.next().toString()
                            if (key == key1) {
                                memberOne[idx][key]!!["value"] = one[key].toString()
                                break
                            }
                        }
                    }
                    //println(memberOne)
                    memberName = one[NAME_KEY].toString()
                    setMyTitle(memberName)
                }
            } else {
                warning(MemberService.msg)
            }
            completion(success)
        }
    }
    fun initIsTeamManager(completion: CompletionHandler) {
        _getTeamManagerList { success ->
            for (i in 0..superDataLists.size-1) {
                val list = superDataLists[i]
                if (list.id == team_id) {
                    isTeamManager = true
                    memberOne.add(hashMapOf("black_list" to hashMapOf("title" to "加入黑名單", "more" to true, "value" to "", "icon" to "blacklist")))
                    break
                }
            }
            completion(success)
        }
    }

    private fun initMemberOne() {
        memberOne.clear()
        memberOne.add(hashMapOf("team" to hashMapOf("title" to team_name, "value" to near_date, "more" to false)))

        for (key in keys) {
            var title: String = ""
            var icon: String = ""
            var existMember: Boolean = false // only if key exist member array add to member one

            for ((key1, value1) in MEMBER_ARRAY) {
                if (key1 == key) {
                    title = value1["text"]!!
                    icon = value1["icon"]!!
                    existMember = true
                }
            }
            if (existMember) {
                var _tmp: HashMap<String, Any> = hashMapOf("icon" to icon,"title" to title,"value" to "","more" to false)
                if (key == MOBILE_KEY) {
                    _tmp["more"] = true
                }
                val tmp:HashMap<String, HashMap<String, Any>> = hashMapOf(key to _tmp)
                memberOne.add(tmp)
            }
        }
        tempPlaySignupOneAdapter.notifyDataSetChanged()
    }

    private fun getRow(rowKey: String): HashMap<String, Any> {
        var res: HashMap<String, Any> = hashMapOf()
        for ((idx, key) in keys.withIndex()) {
            if (key == rowKey) {
                res = memberOne[idx][key]!!
                break
            }
        }
        return res
    }
}

















