package com.sportpassword.bm.Controllers

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.onesignal.OSPermissionObserver
import com.onesignal.OSPermissionStateChanges
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.databinding.ActivityShowPnvcBinding
import me.leolin.shortcutbadger.ShortcutBadger
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class ShowPNVC : MyTableVC(), OSPermissionObserver {

    private lateinit var binding: ActivityShowPnvcBinding
    //private lateinit var view: ViewGroup
    var showTop2: ShowTop2? = null

    var pnArr: JSONArray = JSONArray()
    var isReceive: Boolean = false
    lateinit var thisAdapter: PNAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowPnvcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)
        setContentView(R.layout.activity_show_pnvc)

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

        findViewById<ShowTop2>(R.id.top) ?. let {
            showTop2 = it
            showTop2!!.setTitle("推播訊息")
            it.showPrev(true)
        }

        binding.setupSwitch.setOnCheckedChangeListener { compoundButton, b ->
            //println(b)
            warning("必須透過設定來開啟或關閉是否接收推播功能，無法直接由此處才開啟或關閉接收推播訊息")
        }

        getArr()
        recyclerView = binding.pnList
        refreshLayout = binding.showpnvcRefresh
        setRefreshListener()

        //initAdapter()
        thisAdapter = PNAdapter(this)
        recyclerView.adapter = thisAdapter

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
//        val rows: JSONArray = JSONArray()
//        for (i in pnArr.length()-1 downTo 0) {
//            val row: JSONObject = pnArr.getJSONObject(i)
//            rows.put(row)
//        }
//        println(rows)

//        thisAdapter.rows = rows
        thisAdapter.rows = pnArr
//        var items = arrayListOf<Item>()
        if (pnArr.length() == 0 ) {
            binding.pnEmpty.visibility = View.VISIBLE
        } else {
            binding.pnEmpty.visibility = View.GONE
//            items = generateItems()
        }
        //adapter.update(items)
        //adapter.notifyDataSetChanged()
        thisAdapter.notifyDataSetChanged()
        closeRefresh()

        val pnArr1: JSONArray = JSONArray()
        for (i in 0 until pnArr.length()) {
            val row: JSONObject = pnArr.getJSONObject(i)
            row.put("isShow", true)
            pnArr1.put(row)
        }
        session.edit().putString("pn", pnArr1.toString()).apply()
    }

    fun cellRemove(id: String) {
        warning("是否確定要刪除此訊息？", "關閉", "刪除") {
            MyOneSignal.remove(id)
            refresh()
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
//        println(pnArr)
    }
}

class PNAdapter(val delegate: ShowPNVC): RecyclerView.Adapter<PNViewHolder>() {

    var rows = JSONArray()

    override fun onBindViewHolder(holder: PNViewHolder, position: Int) {

        val row: JSONObject = rows[position] as JSONObject
        if (row.has("id")) {
            holder.idLbl.text = row.getString("id")
        }
        if (row.has("pnid")) {
            holder.pnidLbl.text = row.getString("pnid")
        }
        if (row.has("title")) {
            holder.title.text = row.getString("title")
        }
        if (row.has("content")) {
            holder.content.text = row.getString("content")
        }

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
