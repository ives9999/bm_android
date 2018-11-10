package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.function_item.*
import kotlinx.android.synthetic.main.manager_function_vc.*

class ManagerFunctionVC : MyTableVC() {

    var token: String = ""
    var title: String = ""
    var source: String = "team"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_function_vc)

        title = intent.getStringExtra("title")
        token = intent.getStringExtra("token")
        source = intent.getStringExtra("source")

        setMyTitle(title)

        if (source == "coach") {
            rows = arrayListOf(
                    hashMapOf("text" to "編輯", "icon" to "edit1", "key" to "edit"),
                    hashMapOf("text" to "教球時段編輯", "icon" to "tempplayedit"),
                    hashMapOf("text" to "報名學員名單", "icon" to "tempplaylist"),
                    hashMapOf("text" to "刪除", "icon" to "clear")
            )
        } else if (source == "team") {
            rows = arrayListOf(
                    hashMapOf("text" to "編輯", "icon" to "edit1"),
                    hashMapOf("text" to "臨打編輯", "icon" to "tempplayedit", "key" to "edit_tempplay"),
                    hashMapOf("text" to "每次臨打名單", "icon" to "tempplaylist"),
                    hashMapOf("text" to "刪除", "icon" to "clear")
            )
        } else if (source == "arena") {
            rows = arrayListOf(
                    hashMapOf("text" to "編輯", "icon" to "edit1"),
                    hashMapOf("text" to "時段編輯", "icon" to "tempplayedit"),
                    hashMapOf("text" to "報名球隊名單", "icon" to "tempplaylist"),
                    hashMapOf("text" to "刪除", "icon" to "clear")
            )
        }

        recyclerView = function_list
        initAdapter()
    }

    override fun generateItems(): ArrayList<Item> {
        var items: ArrayList<Item> = arrayListOf()
        for (row in rows) {
            var text = ""
            if (row.containsKey("text")) {
                text = row.get("text")!!
            }
            var icon = ""
            if (row.containsKey("icon")) {
                icon = row.get("icon")!!
            }
            var key = "edit"
            if (row.containsKey("key")) {
                key = row.get("key")!!
            }
            items.add(FunctionItem(source, icon, text, key))
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {
        val functionItem = item as FunctionItem
        val source = functionItem.source
        val key = functionItem.key

        when (source) {
            "coach" -> {
                when (key) {
                    "edit" -> {
                        goEdit(source, title, token)
                    }

                }
            }
            "team" -> {
                when (key) {
                    "edit" -> {
                        goEdit(source, title, token)
                    }
                    "edit_tempplay" -> {
                        goTeamTempPlayEdit(token)
                    }
                }
            }
            "arena" -> {
                when (key) {
                    "edit" -> {
                        goEdit(source, title, token)
                    }
                }
            }
        }
    }

    fun delete(view: View) {
        goDeleteTeam(token)
    }
    fun tempPlay(view: View) {
        goTeamTempPlayEdit(token)
    }
    fun tempPlayDate(view: View) {
        goTempPlayDate(title, token)
    }
}

class FunctionItem(val source: String, val icon: String, val text: String, val key: String): Item() {
    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {
        viewHolder.icon.setImage(icon)
        viewHolder.text.text = text
    }

    override fun getLayout() = R.layout.function_item

}