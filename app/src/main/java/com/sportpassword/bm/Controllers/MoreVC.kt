package com.sportpassword.bm.Controllers

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.core.content.pm.PackageInfoCompat
import com.sportpassword.bm.Adapters.MoreAdapter
import com.sportpassword.bm.Data.MoreRow
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.bottom_view.*
import kotlinx.android.synthetic.main.tab_course.*
import kotlinx.android.synthetic.main.top_view.*
import org.jetbrains.anko.backgroundColor

open class MoreVC : MyTableVC() {

    lateinit var tableAdapter: MoreAdapter
    var moreRows: ArrayList<MoreRow> = arrayListOf()

    private fun initMoreRows(): ArrayList<MoreRow> {
        val rows: ArrayList<MoreRow> = arrayListOf()
        val r1: MoreRow = MoreRow("商品", "product", "product", R.color.MY_LIGHT_RED)
        rows.add(r1)
        val r2: MoreRow = MoreRow("教學", "teach", "teach", R.color.MY_WHITE)
        rows.add(r2)
        val r3: MoreRow = MoreRow("教練", "coach", "coach", R.color.MY_WHITE)
        rows.add(r3)
        val r4: MoreRow = MoreRow("體育用品店", "store", "store", R.color.MY_WHITE)
        rows.add(r4)
        val r5: MoreRow = MoreRow("推播訊息", "pn", "push", R.color.MY_WHITE)
        rows.add(r5)
        val r6: MoreRow = MoreRow("版本", "version", "version", R.color.MY_WHITE)
        rows.add(r6)

        return rows
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        able_type = "more"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_vc)

        moreTabLine.backgroundColor = myColorGreen
        topTitleLbl.setText("更多")

        recyclerView = list_container
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()

        //recyclerView.adapter = adapter
        tableAdapter = MoreAdapter(this)
        moreRows = initMoreRows()
        tableAdapter.moreRow = moreRows
        recyclerView.adapter = tableAdapter

        init()
    }

    override fun init() {
        super.init()
    }

    override fun cellClick(idx: Int) {
        val row: MoreRow = moreRows[idx]
        val key: String = row.key
        when (key) {
            "product"-> this.toProduct()
            "coach"-> this.toCoach()
            "teach"-> this.toTeach()
            "store"-> this.toStore()
            "pn"-> {
                val intent = Intent(activity, ShowPNVC::class.java)
                startActivity(intent)
            }
            "version"-> {
                val p = context.applicationContext.packageManager.getPackageInfo(
                    context.packageName,
                    0
                )
                val v = PackageInfoCompat.getLongVersionCode(p).toInt()
                val n = p.versionName
                val builder = AlertDialog.Builder(context)
                builder.setMessage(n + "#" + v)
                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}
