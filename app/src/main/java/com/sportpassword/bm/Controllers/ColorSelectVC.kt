package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.MYCOLOR
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.color_select_item.*
import kotlinx.android.synthetic.main.mytablevc.*
import org.jetbrains.anko.backgroundColor

class ColorSelectVC: MyTableVC() {

    lateinit var allColors: ArrayList<HashMap<String, Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mytablevc)

        setMyTitle("顏色")

        allColors = MYCOLOR.all()
        recyclerView = mytable
        initAdapter()
    }

    override fun generateItems(): ArrayList<Item> {
        var items: ArrayList<Item> = arrayListOf()
        for (row in allColors) {
            var color = 0x36c6d3
            if (row.containsKey("color")) {
                color = row.get("color") as Int
            }
            var key = "success"
            if (row.containsKey("key")) {
                key = row.get("key") as String
            }
            items.add(ColorItem(color, key))
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {
        val functionItem = item as FunctionItem
        val source = functionItem.source
        val key = functionItem.key


    }
}

class ColorItem(val color: Int, val key: String): Item() {
    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {
        viewHolder.color.backgroundColor = color
    }

    override fun getLayout() = R.layout.color_select_item

}