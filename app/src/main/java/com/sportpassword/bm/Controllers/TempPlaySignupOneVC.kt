package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sportpassword.bm.Adapters.TempPlaySignupOneAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.databinding.ActivityTempPlaySignupOneVcBinding
import org.jetbrains.anko.contentView
import org.jetbrains.anko.runOnUiThread

class  TempPlaySignupOneVC : BaseActivity() {

    private lateinit var binding: ActivityTempPlaySignupOneVcBinding
    private lateinit var view: ViewGroup

    var team_name: String = ""
    var teamToken: String = ""
    var team_id: Int = -1
    var near_date: String = ""
    var status: String = "on"
    var off_at: String = ""

    var isTeamManager: Boolean = false
    var memberName: String = ""
    var memberToken: String = ""
    var memberMobile: String = ""
    var memberOne: ArrayList<HashMap<String, HashMap<String, Any>>> = arrayListOf()
    var keys: ArrayList<String> = arrayListOf("team", NAME_KEY, MOBILE_KEY, "black_list", "temp_play_status")

    lateinit var tempPlaySignupOneAdapter: TempPlaySignupOneAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTempPlaySignupOneVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        if (intent.hasExtra("memberToken")) {
            memberToken = intent.getStringExtra("memberToken")!!
        }

        if (intent.hasExtra("token")) {
            teamToken = intent.getStringExtra("token")!!
        }

        if (intent.hasExtra("name")) {
            team_name = intent.getStringExtra("name")!!
        }

        if (intent.hasExtra("team_id")) {
            team_id = intent.getIntExtra("id", -1)
        }

        if (intent.hasExtra("near_date")) {
            near_date = intent.getStringExtra("near_date")!!
        }

        if (intent.hasExtra("status")) {
            status = intent.getStringExtra("status")!!
        }

        if (intent.hasExtra("off_at")) {
            off_at = intent.getStringExtra("off_at")!!
        }
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
        binding.dataList.adapter = tempPlaySignupOneAdapter

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.tempPlaySignupOne_refresh)
        setRefreshListener()

        refresh()
    }

    override fun refresh() {
        super.refresh()
        initMemberOne()
        Loading.show(view)
        initGetMemberValue { success ->
            if (success) {
                initIsTeamManager { success ->
                    runOnUiThread {
                        Loading.hide(view)
                    }
                    if (status == "off") {
                        memberOne.add(hashMapOf("temp_play_status" to hashMapOf("title" to "取消", "value" to off_at.noSec() as Any)))
                    }
//                    println(memberOne)
                    tempPlaySignupOneAdapter.notifyDataSetChanged()
                    closeRefresh()
                }
            } else {
//                loading.dismiss()
            }
        }
    }

    fun initGetMemberValue(completion: CompletionHandler) {
//        _getMemberOne(memberToken) { success ->
//            if (success) {
//                val one = MemberService.one
//                if (one == null) {
//                    warning("無法取得該會員資訊，請稍後再試")
//                } else {
//                    for ((idx, key) in keys.withIndex()) {
//                        val keys1: Iterator<String> = one.keys()
//                        while (keys1.hasNext()) {
//                            val key1: String = keys1.next().toString()
//                            if (key == key1) {
//                                memberOne[idx][key]!!["value"] = one[key].toString()
//                                break
//                            }
//                        }
//                    }
//                    //println(memberOne)
//                    memberName = one[NAME_KEY].toString()
//                    setMyTitle(memberName)
//                }
//            } else {
//                warning(MemberService.msg)
//            }
//            completion(success)
//        }
    }
    fun initIsTeamManager(completion: CompletionHandler) {
        _getTeamManagerList { success ->
//            for (i in 0..superDataLists.size-1) {
//                val list = superDataLists[i]
//                if (list.id == team_id) {
//                    isTeamManager = true
//                    memberOne.add(hashMapOf("black_list" to hashMapOf("title" to "加入黑名單", "more" to true, "value" to "", "icon" to "blacklist")))
//                    break
//                }
//            }
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

















