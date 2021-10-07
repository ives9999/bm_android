package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.Models.ArenaTable
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.ARENA_KEY
import com.sportpassword.bm.Utilities.Global
import com.sportpassword.bm.Utilities.Loading
import kotlinx.android.synthetic.main.mask.*

class SelectArenaVC : SingleSelectVC() {

    var arenas1: ArrayList<ArenaTable>? = null

//    val mAdapter: MyAdapter = MyAdapter()
//    val adapter1 = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_single_select_vc)

        var city_id: Int? = null
        if (intent.hasExtra("city_id")) {
            city_id = intent.getIntExtra("city_id", 0)
            if (city_id == 0) {
                city_id = null
            }
        }

        key = ARENA_KEY

//        rows.add(hashMapOf("title" to "aaa", "value" to "1"))
//        rows.add(hashMapOf("title" to "bbb", "value" to "2"))

//        for (row in rows) {
//            val item = MyItem(row["title"]!!)
//            adapter.add(item)
//        }

//        tableView.adapter = adapter

        if (city_id != null) {
            Loading.show(mask)
            TeamService.getArenaByCityID(this, city_id) { success ->

                Loading.hide(mask)
                if (success) {
                    arenas1 = TeamService.arenas
                    if (arenas1 != null) {

//                        mAdapter.updateList(rows)
//                        mAdapter.notifyDataSetChanged()

                        tableRows = rowsBridge(arenas1!!)
                        tableAdapter.rows = tableRows
                        tableAdapter.notifyDataSetChanged()
//                        tableView.adapter = tableAdapter
//                        notifyChanged()

//                        for (row in rows) {
//                            val item = SingleSelectItem(row["title"]!!, row["value"]!!, false, {})
//                            adapter1.add(item)
//                        }
//                        rows.add(hashMapOf("title" to "aaa", "value" to "1"))
//                        rows.add(hashMapOf("title" to "bbb", "value" to "2"))
//                        for (row in rows) {
//                            //val item = MyItem(row["title"]!!)
//                            val item = MyItem(row["title"]!!, row["value"]!!, false, {})
//                            adapter.add(item)
//                        }
//                        tableView.adapter = adapter1
//                        adapter.notifyDataSetChanged()

                    } else {
                        warning("無法取得球館資料，請洽管理員")
                    }
                }
            }
            setMyTitle(Global.zoneIDToName(city_id))
        } else {
            warning("沒有傳送縣市代碼，請洽管理員")
        }
    }

    fun rowsBridge(arenas: ArrayList<ArenaTable>): ArrayList<SelectRow> {

        val selectRows: ArrayList<SelectRow> = arrayListOf()

        for(arena in arenas) {
            val title = arena.name
            val id = arena.id
            selectRows.add(SelectRow(title, id.toString()))
        }

        return selectRows
    }

    override fun submit(idx: Int) {

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
            //dealSelected()
            val intent = Intent()
            intent.putExtra("key", key)
            intent.putExtra("show", row.title)
            intent.putExtra("selected", selected)
            intent.putExtra("able_type", able_type)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else { //取消原來的選擇
            selected = ""
            generateItems()
            //notifyChanged()
        }
    }
}

//class MyItem(var title: String): Item() {
//
//    constructor(title: String, name: String) : this(title) {}
//
//    override fun bind(
//        viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder,
//        position: Int
//    ) {
//        title = "aaa"
//        viewHolder.title.text = title
//    }
//
//    override fun getLayout(): Int {
//        return R.layout.select_item
//    }
//
//}
//
//class MyAdapter: RecyclerView.Adapter<MyAdapter.mViewHolder>() {
//
//    var unAssignList: ArrayList<HashMap<String, String>> = arrayListOf()
//
//    inner class mViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//
//        val title: TextView = itemView.title
//
//        fun bind(item: HashMap<String, String>) {
//            title.text = item["title"]
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
//
//        val inflater = LayoutInflater.from(parent.context)
//        val layout = inflater.inflate(R.layout.select_item, parent, false)
//
//        return mViewHolder(layout)
//    }
//
//    override fun getItemCount(): Int {
//
//        return unAssignList.size
//    }
//
//    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
//
//        holder.bind(unAssignList[position])
//    }
//
//    fun updateList(list: ArrayList<HashMap<String, String>>) {
//
//        unAssignList = list
//    }
//}
































