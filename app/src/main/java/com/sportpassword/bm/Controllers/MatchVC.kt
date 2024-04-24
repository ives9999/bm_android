package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.TeamAdapter
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.Models.TeamsTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MatchService
import com.sportpassword.bm.databinding.ActivitySearchVcBinding
import com.sportpassword.bm.member
import org.jetbrains.anko.backgroundColor
import tw.com.bluemobile.hbc.extensions.setInfo
import com.sportpassword.bm.functions.jsonToModels

class MatchVC : MyTableVC() {

    private lateinit var binding: ActivitySearchVcBinding

    var mysTable: TeamsTable? = null
    lateinit var tableAdapter: TeamAdapter


    var mustLoginLbl: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        able_type = "team"

        super.onCreate(savedInstanceState)

        binding = ActivitySearchVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)
        dataService = MatchService
        delegate = this
        recyclerView = binding.listContainer
        recyclerView.setHasFixedSize(true)
        tableAdapter = TeamAdapter(R.layout.team_list_cell, this)
        oneSectionAdapter.setOneSection(oneSections)
        member_like = true
        init()
    }

    override fun init() {
        super.init()

        if (member.isLoggedIn) {
            setListView()
            binding.footer.visibility = View.GONE
            mustLoginLbl?.visibility = View.INVISIBLE
            findViewById<RecyclerView>(R.id.list_container)?.let {
                it.visibility = View.VISIBLE
            }
            member_like = false
            recyclerView.adapter = tableAdapter
            params.clear()
            refresh()
        } else {
            showNothingInfo("請先登入")
        }
    }


    override fun genericTable() {
        try {
            mysTable = jsonToModels<TeamsTable>(jsonString!!)
        } catch (e: JsonParseException) {
            println(e.localizedMessage)
        }
        if (mysTable != null) {
            tables = mysTable
            if (mysTable!!.rows.size > 0) {
                getPage()
                tableLists += generateItems1(TeamTable::class, mysTable!!.rows)
                tableAdapter.setMyTableList(tableLists)
                runOnUiThread {
                    tableAdapter.notifyDataSetChanged()
                }
            } else {
                showNothingInfo("目前暫無資料")
            }
        }
    }

    private fun showNothingInfo(info: String) {
        runOnUiThread {
            findViewById<LinearLayout>(R.id.tableViewContainer)?.let {
                findViewById<RecyclerView>(R.id.list_container)?.let {
                    it.visibility = View.GONE
                }
                mustLoginLbl = it.setInfo(this, info)
            }
        }
    }

    private fun setListView() {
        val params: ViewGroup.MarginLayoutParams =
            binding.tableViewContainer.layoutParams as ViewGroup.MarginLayoutParams
        params.marginStart = 0
        params.marginEnd = 0
        binding.tableViewContainer.layoutParams = params

        val color = ContextCompat.getColor(this, android.R.color.transparent)
        binding.tableViewContainer.backgroundColor = color
    }
}