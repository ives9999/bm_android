package com.sportpassword.bm.Controllers

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.bottom_view.*
import org.jetbrains.anko.backgroundColor

class SearchVC : MyTableVC() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_vc)

        teamTabLine.backgroundColor = myColorGreen
    }
}