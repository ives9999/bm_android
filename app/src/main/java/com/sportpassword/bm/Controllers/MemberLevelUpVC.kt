package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sportpassword.bm.Adapters.MemberLevelUpAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.activity_member_coin_list_vc.*

class MemberLevelUpVC : MyTableVC() {

    lateinit var tableAdapter: MemberLevelUpAdapter

    var bottom_button_count: Int = 3
    val button_width: Int = 400

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_level_up_vc)

        setMyTitle("進階會員")

        val rows: ArrayList<OneRow> = arrayListOf()
        val row: OneRow = OneRow()
        row.title = "金牌會員"
        rows.add(row)

        recyclerView = list
        tableAdapter = MemberLevelUpAdapter(this)
        recyclerView.adapter = tableAdapter
        //tableAdapter.setMyTableList(rows)

        setBottomThreeView()
    }

    fun setBottomThreeView() {
        findViewById<Button>(R.id.submitBtn) ?. let {
            it.text = "購買點數"
            it.setOnClickListener {
                toProduct()
            }
        }

        findViewById<Button>(R.id.threeBtn) ?. let {
            it.text = "退款"
            it.setOnClickListener {
                //coinReturn()
            }
        }

        findViewById<Button>(R.id.cancelBtn) ?. let {
            it.text = "回上一頁"
            it.setOnClickListener {
                prev()
            }
        }

        setBottomButtonPadding()
    }

    fun setBottomButtonPadding() {

        val padding: Int = (screenWidth - bottom_button_count * button_width) / (bottom_button_count + 1)
        //val leading: Int = bottom_button_count * padding + (bottom_button_count - 1) * button_width

        findViewById<Button>(R.id.submitBtn) ?. let {
            if (it.visibility == View.VISIBLE) {
                val params: ViewGroup.MarginLayoutParams =
                    it.layoutParams as ViewGroup.MarginLayoutParams
                params.width = button_width
                params.marginStart = padding
                it.layoutParams = params
            }
        }

        findViewById<Button>(R.id.threeBtn) ?. let {
            if (it.visibility == View.VISIBLE) {
                val params: ViewGroup.MarginLayoutParams =
                    it.layoutParams as ViewGroup.MarginLayoutParams
                params.width = button_width
                params.marginStart = padding
                it.layoutParams = params
            }
        }

        findViewById<Button>(R.id.cancelBtn) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }
    }
}