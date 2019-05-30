package com.sportpassword.bm.Controllers

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_single_select_vc.*
import kotlinx.android.synthetic.main.select_item.*

class SingleSelectVC : MyTableVC() {

    var title: String = "選擇"
    var key: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_select_vc)

        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")
        }
        setMyTitle(title)

        if (intent.hasExtra("rows")) {
            rows = intent.getSerializableExtra("rows") as ArrayList<HashMap<String, String>>
        }

        recyclerView = tableView
        initAdapter()

        if (intent.hasExtra("key")) {
            key = intent.getStringExtra("key")
        }
        if (key == null) {
            alertError()
        }
    }

    override fun generateItems(): ArrayList<Item> {

        var items: ArrayList<Item> = arrayListOf()

        val rowClick = { i: Int ->
            submit(i)
        }
        for (row in rows) {
            var title: String? = null
            if (row.containsKey("title")) {
                title = row.get("title")!!
            }
            var value: String? = null
            if (row.containsKey("value")) {
                value = row.get("value")!!
            }
            if (title != null && value != null) {
                val item = SingleSelectItem(title, value, rowClick)
                items.add(item)
            }
        }

        return items
    }

    fun submit(idx: Int) {

        if (idx >= rows.size) {
            alertError()
        }
        val row = rows[idx]
        if (!row.containsKey("value")) {
            alertError()
        }
        val value = row.get("value")!!
        val intent = Intent()
        intent.putExtra("key", key)
        intent.putExtra("selected", value)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun alertError() {
        val alert = AlertDialog.Builder(this).create()
        alert.setTitle("警告")
        alert.setMessage("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "回上一頁", { Interface, j ->
            finish()
        })
        alert.show()
    }
}

class SingleSelectItem(val title: String, val value: String, val rowClick:(idx: Int)->Unit): Item() {


    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

        viewHolder.title.text = title
        viewHolder.container.setOnClickListener {
            rowClick(position)
        }
    }

    override fun getLayout() = R.layout.select_item
}