package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.MYCOLOR
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.color_select_item.*
import kotlinx.android.synthetic.main.mytablevc.*
import org.jetbrains.anko.backgroundColor

class ColorSelectVC: MyTableVC() {

    lateinit var key: String
    lateinit var all: ArrayList<HashMap<String, Any>>
    var selecteds: ArrayList<MYCOLOR> = arrayListOf()
    //選擇的類型：just one單選，multi複選
    var select: String = "just one"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mytablevc)
        setMyTitle("顏色")

        if (intent.hasExtra("selecteds")) {
            selecteds = intent.getSerializableExtra("selecteds") as ArrayList<MYCOLOR>
        }
        key = intent.getStringExtra("key")!!

        all = MYCOLOR.all()
        recyclerView = mytable
        //initAdapter()
    }

    override fun generateItems(): ArrayList<Item> {
        var items: ArrayList<Item> = arrayListOf()
        for (row in all) {
            var color = 0x36c6d3
            if (row.containsKey("color")) {
                color = row.get("color") as Int
            }
            var key = "success"
            if (row.containsKey("key")) {
                key = row.get("key") as String
            }
            var value: MYCOLOR? = null
            if (row.containsKey("value")) {
                value = row.get("value") as MYCOLOR
            }
            var isSelected = false
            if (value != null) {
                for (selected in selecteds) {
                    if (value == selected) {
                        isSelected = true
                        break
                    }
                }
            }
            items.add(ColorItem(color, key, isSelected))
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<GroupieViewHolder>, view: View) {
        val colorItem = item as ColorItem
        val key = colorItem.key
        val colorType = MYCOLOR.from(key)

        var isExist = false
        var at = 0
        for ((idx, selectColor) in selecteds.withIndex()) {
            if (selectColor == colorType) {
                isExist = true
                at = idx
                break
            }
        }
        if (isExist) {
            selecteds.removeAt(at)
        } else {
            if (select == "just one") {
                selecteds.clear()
            }
            selecteds.add(colorType)
        }
        if (select == "just one" && selecteds.size > 0) {
            submit()
        }
    }

    fun submit() {
        val intent = Intent()
        if (select == "multi") {
            if (selecteds.size == 0) {
                warning("請至少選擇一個選項")
                return
            }
        }
        intent.putExtra("key", key)
        intent.putExtra("selecteds", selecteds)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

class ColorItem(val color: Int, val key: String, val isSelected: Boolean): Item() {
    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
        viewHolder.color.backgroundColor = color
        if (isSelected) {
            viewHolder.isSelected.visibility = View.VISIBLE
        } else {
            viewHolder.isSelected.visibility = View.INVISIBLE
        }
    }

    override fun getLayout() = R.layout.color_select_item

}