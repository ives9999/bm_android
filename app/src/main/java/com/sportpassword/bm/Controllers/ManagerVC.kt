package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.View
import com.sportpassword.bm.Adapters.ManagerAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.CHANNEL
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.manager_vc.*
import kotlinx.android.synthetic.main.mask.*
import org.jetbrains.anko.contentView

class ManagerVC : BaseActivity() {

    lateinit var managerAdapter: ManagerAdapter
    var source: String = "team"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_vc)
        if (intent.hasExtra("source")) {
            source = intent.getStringExtra("source")
        }
        if (source == "team") {
            setMyTitle("球隊管理")
        } else if (source == "coach") {
            setMyTitle("教練管理")
        } else if (source == "arena") {
            setMyTitle("球館管理")
        } else {
            setMyTitle("球隊管理")
        }

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.tempManager_refresh)
        setRefreshListener()
        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add, menu)
        return true
    }

    override fun refresh() {
        getManagerList()
    }

    private fun getManagerList() {
        val filter1: Array<Any> = arrayOf("channel", "=", CHANNEL)
        val filter2: Array<Any> = arrayOf("manager_id", "=", member.id)
        val filter: Array<Array<Any>> = arrayOf(filter1, filter2)

        Loading.show(mask)
        dataService.getList(this, source, "name", hashMapOf<String, Any>(), 1, 100, filter) { success ->
            if (success) {
                managerAdapter = ManagerAdapter(this, dataService.superDataLists,
                        { title, token ->
                            goManagerFunction(title, token, source)
                        }
                )
                manager_list.adapter = managerAdapter
                closeRefresh()
            }
            Loading.hide(mask)
        }
    }

    fun add(view: View) {
        if (member.validate < 1) {
            Alert.show(this@ManagerVC, "錯誤", "未通過EMail認證，無法新增球隊，認證完後，請先登出再登入")
        } else {
            goEdit()
        }
    }
}
