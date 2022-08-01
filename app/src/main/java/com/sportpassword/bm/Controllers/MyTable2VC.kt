package com.sportpassword.bm.Controllers

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Adapters.MemberLevelUpViewHolder
//import com.sportpassword.bm.Adapters.MemberLevelUpAdapter
import com.sportpassword.bm.Adapters.MyAdapter2
import com.sportpassword.bm.Adapters.MyViewHolder2
import com.sportpassword.bm.Models.MemberLevelKindTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.Utilities.Global
import com.sportpassword.bm.Utilities.jsonToModels2
import java.lang.reflect.Type
import kotlin.reflect.typeOf

class MyTable2VC<T: MyViewHolder2<U>, U: Table>(recyclerView: RecyclerView, cell: Int, private val viewHolderConstructor: (Context, View, List1CellDelegate?)-> T, private val type: Type, list1CellDelegate: List1CellDelegate?) {

    //var recyclerView: RecyclerView
    val adapter: MyAdapter2<T, U>
    var msg: String = ""

    init {
        //recyclerView = findViewById<RecyclerView>(resource)
        adapter = MyAdapter2<T, U>(cell, viewHolderConstructor, list1CellDelegate)
        recyclerView.adapter = adapter
    }

    fun parseJSON(jsonString: String): Boolean {
        val rows: ArrayList<U> = genericTable2(jsonString)
        if (rows.size == 0) {
            return false
        } else {
            setItems(rows)
        }

        return true
    }

    private fun genericTable2(jsonString: String): ArrayList<U> {
        val rows: ArrayList<U> = arrayListOf()

        //val type = object : TypeToken<Tables2<U>>() {}.type

        //val a = Gson().fromJson<U>(jsonString, type)
        //val tables2: Tables2<U>? = jsonToModels2<Tables2<U>, U>(jsonString, type)
        //val a = Gson().fromJson<U>(jsonString)

        //var tables2: Tables2<MemberLevelKindTable>? = null
        val mysTable = jsonToModels2<Tables2<MemberLevelKindTable>, MemberLevelKindTable>(jsonString!!, type)

        try {
            val tables2 = Gson().fromJson<Tables2<MemberLevelKindTable>>(jsonString, type)
            val n = 6
        } catch (e: java.lang.Exception) {
            Global.message = e.localizedMessage
            println(e.localizedMessage)
        }

//        if (tables2 == null) {
//            msg = "無法從伺服器取得正確的json資料，請洽管理員"
//        } else {
//            if (tables2.success) {
//                if (tables2.rows.size > 0) {
//                    //rows.addAll(tables2.rows)
//                }
//            } else {
//                msg = "解析JSON字串時，沒有成功，系統傳回值錯誤，請洽管理員"
//            }
//        }

        return rows
    }

    fun setItems(rows: ArrayList<U>) {
        adapter.items = rows
    }

    fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }
}
