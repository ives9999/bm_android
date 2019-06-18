package com.sportpassword.bm.Controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageButton
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
import com.sportpassword.bm.Utilities.CITY_KEY
import com.sportpassword.bm.Utilities.IndexPath
import com.sportpassword.bm.Utilities.KEYWORD_KEY
import kotlinx.android.synthetic.main.activity_coach_vc.*

class CoachVC : MoreVC() {

    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY),
            hashMapOf("title" to "縣市","detail" to "全部","key" to CITY_KEY)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coach_vc)

        setMyTitle("教練")
        searchRows = _searchRows
        recyclerView = coach_list
        dataService = CoachService
        refreshLayout = coach_refresh
        initAdapter()

        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_manager, menu)
        val memuView = menu!!.findItem(R.id.menu_search_manager).actionView

        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        val ManagerBtn = memuView.findViewById<ImageButton>(R.id.manager)

        searchBtn.tag = type
        ManagerBtn.tag = type

        return true
    }

    override fun remove(indexPath: IndexPath) {
        val row = _searchRows[indexPath.row]
        val key = row["key"]!!
        when (key) {
            CITY_KEY -> citys.clear()
        }
        _searchRows[indexPath.row]["detail"] = "全部"
        val rows = generateSearchItems(type!!)
        searchAdapter.update(rows)
    }
}
