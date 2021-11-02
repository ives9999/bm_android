package com.sportpassword.bm.Controllers

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.onesignal.OSPermissionObserver
import com.onesignal.OSPermissionStateChanges
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_show_pnvc.*
import kotlinx.android.synthetic.main.show_pnvc_item.*
import me.leolin.shortcutbadger.ShortcutBadger
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class ShowPNVC : MyTableVC(), OSPermissionObserver {

    var pnArr: JSONArray = JSONArray()
    var isReceive: Boolean = false
    lateinit var thisAdapter: PNAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_pnvc)
        setMyTitle("推播訊息")

        //val notificationServiceExtension = NotificationServiceExtension()

        //OneSignal.clearOneSignalNotifications()

//        try {
//            pn_id = intent.getStringExtra("id")
//            //OneSignal.cancelNotification()
//        } catch (e: Exception) {
//
//        }
//        try {
//            title = intent.getStringExtra("title")
//        } catch (e: Exception) {
//
//        }
//        try {
//            content = intent.getStringExtra("content")
//        } catch (e: Exception) {
//
//        }

//        val status = OneSignal.getPermissionSubscriptionState()
//        isReceive = status.permissionStatus.enabled
//        //println("hasPrompt status is $hasPrompt")
//        setupSwitch.isChecked = isReceive

        setupSwitch.setOnCheckedChangeListener { compoundButton, b ->
            //println(b)
            warning("必須透過設定來開啟或關閉是否接收推播功能，無法直接由此處才開啟或關閉接收推播訊息")
        }

        getArr()
        recyclerView = pn_list
        refreshLayout = showpnvc_refresh
        setRefreshListener()

        //initAdapter()
        thisAdapter = PNAdapter(this)

        init()
        refresh()

        ShortcutBadger.applyCount(this, 0)
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        isSearchIconShow = false
//        super.onCreateOptionsMenu(menu)
//
//        return true
//    }

//    override fun initAdapter(include_section: Boolean) {
        //adapter = GroupAdapter()
        //recyclerView.adapter = adapter
//        refresh()
//    }

    override fun refresh() {
        getArr()
        thisAdapter.rows = pnArr
//        var items = arrayListOf<Item>()
        if (pnArr.length() == 0 ) {
            pn_empty.visibility = View.VISIBLE
        } else {
            pn_empty.visibility = View.GONE
//            items = generateItems()
        }
        //adapter.update(items)
        //adapter.notifyDataSetChanged()
        closeRefresh()
    }

    fun cellRemove(id: String) {
        warning("是否確定要刪除此訊息？", "關閉", "刪除") {
            MyOneSignal.remove(id)
            refresh()
            thisAdapter.notifyDataSetChanged()
        }
    }

//    override fun generateItems(): ArrayList<Item> {
//        val items: ArrayList<Item> = arrayListOf()
//        for (i in 0..pnArr.length()-1) {
//            val j = pnArr.length()-1-i
//            val obj = pnArr[j] as JSONObject
//            var id = ""
//            if (obj.has("id")) {
//                id = obj.getString("id")
//            }
//            var pnid = ""
//            if (obj.has("pnid")) {
//                pnid = obj.getString("pnid")
//            }
//            var title = ""
//            if (obj.has("title")) {
//                title = obj.getString("title")
//            }
//            val content = obj.getString("content")
//            items.add(PNItem(id, title, content, pnid, { id ->
////                println(id)
//                warning("是否確定要刪除此訊息？", "關閉", "刪除") {
//                    MyOneSignal.remove(id)
//                    refresh()
//                }
//            }))
//        }
//
//        return items
//    }

    fun clear(view: View) {
        //println("clear")
        warning("是否確定要刪除全部訊息？", "關閉", "刪除") {
            MyOneSignal.clear()
            refresh()
        }
    }

    override fun onOSPermissionChanged(stateChanges: OSPermissionStateChanges?) {
        if (stateChanges != null) {
            //isReceive = stateChanges.from.enabled
            //switch change status
            //setupSwitch.isChecked = isReceive
        }
    }

    private fun getArr() {
//        if (pnArr != null && pnArr!!.length() > 0) {
//            pnArr = null
//        }
        val session: SharedPreferences = this.getSharedPreferences(SESSION_FILENAME, 0)
        val pnStr = session.getString("pn", "")!!
        if (pnStr.length > 0) {
            try {
                pnArr = JSONArray(pnStr)
            } catch (e: Exception) {
                //println(e.localizedMessage)
            }
        } else {
            pnArr = JSONArray()
        }
    }
}

class PNAdapter(val delegate: ShowPNVC): RecyclerView.Adapter<PNViewHolder>() {

    var rows = JSONArray()

    override fun onBindViewHolder(holder: PNViewHolder, position: Int) {

        val row: JSONObject = rows[position] as JSONObject
        holder.idLbl.text = row.getString("id")
        holder.pnidLbl.text = row.getString("pnid")
        holder.title.text = row.getString("title")
        holder.content.text = row.getString("content")

        holder.pnRemove.setOnClickListener {
            delegate.cellRemove(row.getString("id"))
        }
    }

    override fun getItemCount(): Int {
        return rows.length()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PNViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.show_pnvc_item, parent, false)

        return PNViewHolder(viewHolder)
    }
}

class PNViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
    val idLbl: TextView = viewHolder.findViewById(R.id.idLbl)
    val pnidLbl: TextView = viewHolder.findViewById(R.id.pnidLbl)
    val title: TextView = viewHolder.findViewById(R.id.titleLbl)
    val content: TextView = viewHolder.findViewById(R.id.contentLbl)
    val pnRemove: ImageView = viewHolder.findViewById(R.id.pn_remove)
}

//class PNItem(val id: String, val title: String, val content: String, val pnID: String, val removeClick:(String)->Unit): Item() {
//
//    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
//        viewHolder.idLbl.setText(id)
//        viewHolder.pnidLbl.setText(pnID)
//        if (title.length > 0) {
//            viewHolder.titleLbl.visibility = View.VISIBLE
//            viewHolder.titleLbl.setText(title)
//        } else {
//            viewHolder.titleLbl.visibility = View.GONE
//        }
//        viewHolder.contentLbl.setText(content)
//
//        viewHolder.pn_remove.setOnClickListener {
//            //println(id)
//            removeClick(id)
//        }
//    }
//
//    override fun getLayout() = R.layout.show_pnvc_item
//
//}
