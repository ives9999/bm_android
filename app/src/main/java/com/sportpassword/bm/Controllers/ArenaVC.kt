package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService
import kotlinx.android.synthetic.main.activity_arena_vc.*
import com.sportpassword.bm.Utilities.*


class ArenaVC : MoreVC() {

    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY),
        hashMapOf("title" to "縣市","detail" to "全部","key" to CITY_KEY),
        hashMapOf("title" to "區域","detail" to "全部","key" to AREA_KEY),
        hashMapOf("title" to "空調","detail" to "全部","key" to ARENA_AIR_CONDITION_KEY,"switch" to "true"),
        hashMapOf("title" to "盥洗室","detail" to "全部","key" to ARENA_BATHROOM_KEY,"switch" to "true"),
        hashMapOf("title" to "停車場","detail" to "全部","key" to ARENA_PARKING_KEY,"switch" to "true")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arena_vc)
        setMyTitle("球館")

        searchRows = _searchRows
        recyclerView = arena_list
        dataService = ArenaService
        refreshLayout = arena_refresh
        initAdapter()

        refresh()
    }
}
