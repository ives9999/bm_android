package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService
import kotlinx.android.synthetic.main.activity_arena_vc.*

class ArenaVC : MoreVC() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arena_vc)
        setMyTitle("球館")
        recyclerView = arena_list
        dataService = ArenaService
        refreshLayout = arena_refresh
        initAdapter()

        refresh()
    }
}
