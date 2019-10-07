package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.STATUS
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.mytablevc.*
import kotlinx.android.synthetic.main.status_select_item.*

class StatusSelectVC1 : MyTableVC1() {

    lateinit var key: String
    lateinit var all: ArrayList<HashMap<String, Any>>

    var selected: STATUS? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mytablevc)
        setMyTitle("狀態")

        selected = intent.getSerializableExtra("selected") as STATUS
        key = intent.getStringExtra("key")

        all = STATUS.all()
        recyclerView = mytable
        initAdapter()
    }

    override fun generateItems(): ArrayList<Item> {
        var items: ArrayList<Item> = arrayListOf()
        for (row in all) {
            var ch = ""
            if (row.containsKey("ch")) {
                ch = row.get("ch") as String
            }
            var key = "online"
            if (row.containsKey("key")) {
                key = row.get("key") as String
            }
            var value: STATUS? = null
            if (row.containsKey("value")) {
                value = row.get("value") as STATUS
            }
            var isSelected = if(value == selected) true else false

            items.add(StatusItem(ch, key, isSelected))
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {

        val StatusItem = item as StatusItem
        val key = StatusItem.key
        val statusType = STATUS.from(key)

        if (statusType == selected) {
            selected = null
        } else {
            selected = statusType
        }

        submit()
    }

    fun submit() {
        val intent = Intent()
        if (selected == null) {
            warning("請至少選擇一個選項")
            return
        }
        intent.putExtra("key", key)
        intent.putExtra("selected", selected)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

class StatusItem(val ch: String, val key: String, val isSelected: Boolean): Item() {
    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {
        viewHolder.status.text = ch
        if (isSelected) {
            viewHolder.isSelected.visibility = View.VISIBLE
        } else {
            viewHolder.isSelected.visibility = View.INVISIBLE
        }
    }

    override fun getLayout() = R.layout.status_select_item

}
