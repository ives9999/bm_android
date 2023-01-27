package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.sportpassword.bm.Adapters.SingleSelectAdapter
import com.sportpassword.bm.databinding.ActivitySingleSelectVcBinding

interface SingleSelectDelegate {
    fun singleSelected(key: String, selected: String)
}

open class SingleSelectVC : SelectVC() {

    private lateinit var binding: ActivitySingleSelectVcBinding
    lateinit var view: ViewGroup

    var selected: String? = null
    lateinit var tableAdapter: SingleSelectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivitySingleSelectVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("selected")) {
            selected = intent.getStringExtra("selected")
        }

        if (intent.hasExtra("able_type")) {
            able_type = intent.getStringExtra("able_type")!!
        }

        recyclerView = binding.tableView
        tableAdapter = SingleSelectAdapter(selected, this)
        recyclerView.adapter = tableAdapter
//        recyclerView.adapter = adapter
//        initAdapter()
//
        init()
    }

//    override fun generateItems(): ArrayList<Item> {
//
//        val items: ArrayList<Item> = arrayListOf()
//        val rowClick = { i: Int ->
//            submit(i)
//        }
//        for (row in rows) {
//            var title: String? = null
//            if (row.containsKey("title")) {
//                title = row.get("title")!!
//            }
//            var value: String? = null
//            if (row.containsKey("value")) {
//                value = row.get("value")!!
//            }
//            val isSelected = if(value == selected) true else false
//
//            if (title != null && value != null) {
//                val item = SingleSelectItem(title, value, isSelected, rowClick)
//                items.add(item)
//            }
//        }
//
//        return items
//    }

    override fun cellClick(idx: Int) {
        submit(idx)
    }

    open fun submit(idx: Int) {
        if (idx >= tableRows.size) {
            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        }
        val row = tableRows[idx]
        if (row.value.isEmpty()) {
            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        }

        var cancel: Boolean = false
        if (selected != null && selected!!.isNotEmpty()) {
            if (selected == row.value) {
                cancel = true
            }
        }

        //選擇其他的
        if (!cancel) {
            selected = row.value
            val intent = Intent()
            intent.putExtra("key", key)
            intent.putExtra("selected", selected)
            intent.putExtra("able_type", able_type)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else { //取消原來的選擇
            selected = ""
            tableAdapter.selected = selected
            tableAdapter.notifyItemChanged(idx)
        }
    }

//    open fun submit(idx: Int) {
//
//        if (idx >= rows.size) {
//            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
//        }
//        val row = rows[idx]
//        if (!row.containsKey("value")) {
//            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
//        }
//
//        var cancel: Boolean = false
//        if (selected != null && selected!!.isNotEmpty()) {
//            if (selected == row["value"]) {
//                cancel = true
//            }
//        }
//
//        //選擇其他的
//        if (!cancel) {
//            selected = row["value"]
//            //dealSelected()
//            val intent = Intent()
//            intent.putExtra("key", key)
//            intent.putExtra("selected", selected)
//            intent.putExtra("able_type", able_type)
//            setResult(Activity.RESULT_OK, intent)
//            finish()
//        } else { //取消原來的選擇
//            selected = ""
////            generateItems()
////            notifyChanged()
//        }
//    }

    //selected 有時候是56.0要把它處理成56
    open fun dealSelected() {}
}