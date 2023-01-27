package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.ViewGroup
import com.sportpassword.bm.R
import com.sportpassword.bm.databinding.ActivityBlackListVcBinding
import com.sportpassword.bm.databinding.ManagerFunctionVcBinding
import com.sportpassword.bm.databinding.MytablevcBinding

class ManagerFunctionVC1 : MyTableVC() {

    private lateinit var binding: ManagerFunctionVcBinding
    private lateinit var view: ViewGroup

    var token: String = ""
    var title: String = ""
    var source: String = "team"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ManagerFunctionVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        title = intent.getStringExtra("title")!!
        token = intent.getStringExtra("token")!!
        source = intent.getStringExtra("source")!!

        setMyTitle(title)

//        if (source == "coach") {
//            rows = arrayListOf(
//                    hashMapOf("text" to "編輯","icon" to "edit1","key" to "edit"),
//                    hashMapOf("text" to "教球時段編輯","icon" to "calendar","key" to "calendar"),
////                    hashMapOf("text" to "報名學員名單","icon" to "list","key" to "signup_list"),
//                    hashMapOf("text" to "刪除","icon" to "clear","key" to "delete"),
//                    hashMapOf("text" to "課程","icon" to "course","key" to "course")
//            )
//        } else if (source == "team") {
//            rows = arrayListOf(
//                    hashMapOf("text" to "編輯","icon" to "edit1"),
//                    hashMapOf("text" to "臨打編輯","icon" to "tempplayedit", "key" to "edit_tempplay"),
//                    hashMapOf("text" to "每次臨打名單","icon" to "tempplaylist","key" to "tempplay_list"),
//                    hashMapOf("text" to "刪除","icon" to "clear","key" to "delete")
//            )
//        } else if (source == "arena") {
//            rows = arrayListOf(
//                    hashMapOf("text" to "編輯","icon" to "edit1","key" to "edit"),
//                    hashMapOf("text" to "時段編輯","icon" to "tempplayedit","key" to "calendar"),
//                    hashMapOf("text" to "預訂名單","icon" to "list","key" to "signup_list"),
//                    hashMapOf("text" to "刪除","icon" to "clear","key" to "delete")
//            )
//        }

        recyclerView = binding.functionList
        //initAdapter()
    }

//    override fun generateItems(): ArrayList<Item> {
//        var items: ArrayList<Item> = arrayListOf()
//        for (row in rows) {
//            var text = ""
//            if (row.containsKey("text")) {
//                text = row.get("text")!!
//            }
//            var icon = ""
//            if (row.containsKey("icon")) {
//                icon = row.get("icon")!!
//            }
//            var key = "edit"
//            if (row.containsKey("key")) {
//                key = row.get("key")!!
//            }
//            items.add(FunctionItem(source, icon, text, key))
//        }
//
//        return items
//    }

//    override fun rowClick(item: com.xwray.groupie.Item<GroupieViewHolder>, view: View) {
//        val functionItem = item as FunctionItem
//        val source = functionItem.source
//        val key = functionItem.key
//
//        when (source) {
//            "coach" -> {
//                when (key) {
//                    "edit" -> toEdit(source, title, token)
//                    "calendar" -> toTimeTable(source, token)
//                    "signup_list" -> toTempPlayDate(title, token)
//                    //"delete" -> toDelete(source, token)
//                    //"course" -> toCourse(title, token)
//                }
//            }
//            "team" -> {
//                when (key) {
//                    "edit" -> toEdit(source, title, token)
//                    "edit_tempplay" -> toTeamTempPlayEdit(token)
//                    "tempplay_list" -> toTempPlayDate(title, token)
//                    //"delete" -> toDelete(source, token)
//                }
//            }
//            "arena" -> {
//                when (key) {
//                    "edit" -> toEdit(source, title, token)
//                    "calendar" -> toEdit(source, title, token)
//                    "signup_list" -> toTempPlayDate(title, token)
//                    //"delete" -> toDelete(source, token)
//                }
//            }
//        }
//    }
}

//class FunctionItem(val source: String, val icon: String, val text: String, val key: String): Item() {
//    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
//        viewHolder.icon.setImage(icon)
//        viewHolder.text.text = text
//    }
//
//    override fun getLayout() = R.layout.function_item
//
//}
