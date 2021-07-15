package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.sportpassword.bm.Adapters.CollectionAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeachService
import com.sportpassword.bm.Services.TeamService
import kotlinx.android.synthetic.main.activity_teach_vc.*
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.mask.*

class TeachVC : MyTableVC() {

    //lateinit var collectionAdapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        this.dataService = TeachService
        able_type = "teach"

        searchRows = arrayListOf(
            hashMapOf("title" to "標題關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to "")
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teach_vc)

        setMyTitle("教學")

        if (intent.hasExtra("params")) {
            val t = intent.getSerializableExtra("params")
            if (t != null) {
                params = t as HashMap<String, Any>
            }
        }

        recyclerView = teach_list
        refreshLayout = teach_refresh
        initAdapter()

        refresh()
    }
}
