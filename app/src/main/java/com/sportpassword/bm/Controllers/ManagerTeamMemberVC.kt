package com.sportpassword.bm.Controllers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Interface.MyTable2IF
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.Models.TeamMemberTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.Top
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_member_coin_list_vc.*
import kotlinx.android.synthetic.main.mask.*
import java.lang.reflect.Type

open class ManagerTeamMemberVC : BaseActivity(), MyTable2IF {

    var token: String? = null
    var top: Top? = null

    private val tableType: Type = object : TypeToken<Tables2<TeamMemberTable>>() {}.type
    lateinit var tableView: MyTable2VC<TeamMemberViewHolder, TeamMemberTable, ManagerTeamMemberVC>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_team_member)

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")!!
        }

        //setMyTitle("球隊隊員")
        findViewById<Top>(R.id.top) ?. let {
            top = it
            it.showPrev(true)
            it.setTitle("參加球隊")
        }

        findViewById<ImageView>(R.id.scanIV) ?. let {
            it.setOnClickListener {
                handleScan()
            }
        }

        val recyclerView: RecyclerView = findViewById(R.id.list)
        tableView = MyTable2VC(recyclerView, R.layout.team_member_list_cell, ::TeamMemberViewHolder, tableType, this::tableViewSetSelected, this)
        //tableView.adapter = TeamMemberAdapter(R.layout.team_member_list_cell, ::TeamMemberViewHolder, this::didSelect, this::tableViewSetSelected)
        refreshLayout = list_refresh

        init()
        refresh()
    }

    private fun handleScan() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!isPermissionGranted(this, Manifest.permission.CAMERA)) {
                askForPermission(this, android.Manifest.permission.CAMERA, REQUEST_CAMERA)
            }
        } else {
            toScanQRCode()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toScanQRCode()
            }
        }
    }

    override fun init() {
        super.init()
    }

    override fun refresh() {

        page = 1
        getDataFromServer()
//        theFirstTime = true
//        tableLists.clear()
//        getDataStart(page, perPage, member.token)
    }

    private fun getDataFromServer() {
        Loading.show(mask)
        loading = true

        TeamService.teamMemberList(this, token!!, tableView.page, tableView.perPage) { success ->
            runOnUiThread {
                Loading.hide(mask)

                //MyTable2IF
                val b: Boolean = showTableView(tableView, TeamService.jsonString)
                if (b) {
                    tableView.notifyDataSetChanged()
                } else {
                    val rootView: ViewGroup = getRootView()
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
            //showTableView(myTable, MemberService.jsonString)
        }
    }

    private fun addTeamMember(member_token: String) {
        Loading.show(mask)
        loading = true

        TeamService.addTeamMember(this, token!!, member_token, member.token!!) { success ->
            runOnUiThread {
                Loading.hide(mask)
                if (success) {
                    var successTable: SuccessTable? = null
                    try {
                        //println(TeamService.jsonString)
                        successTable = jsonToModel<SuccessTable>(TeamService.jsonString)
                        if (successTable!!.success) {
                            refresh()
                        } else {
                            val msgs: ArrayList<String> = successTable.msgs
                            var msg: String = ""
                            for (temp in msgs) {
                                msg += temp + "\n"
                            }
                            warning(msg)
                        }
                    } catch (e: JsonParseException) {
                        warning(e.localizedMessage!!)
                        //println(e.localizedMessage)
                    }
                } else {
                    warning("新增失敗")
                }
            }
        }
    }

    fun deleteTeamMember(row: TeamMemberTable) {
        warning("確定要刪除嗎？", true, "刪除") {
            Loading.show(mask)
            loading = true

            TeamService.deleteTeamMember(this, row.token) { success ->
                runOnUiThread {
                    Loading.hide(mask)
                    if (success) {
                        refresh()
                    } else {
                        warning("刪除失敗")
                    }
                }
            }
        }
    }

    private fun tableViewSetSelected(row: TeamMemberTable): Boolean { return false }

    private fun toScanQRCode() {
        val i: Intent = Intent(this, ScanQRCodeVC::class.java)
        scanQRCodeVC.launch(i)
    }

    private val scanQRCodeVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {
            val i: Intent? = res.data
            i.apply {
                if (i!!.hasExtra("token")) {
                    val member_token: String = i.getStringExtra("token")!!
                    //println(member_token)
                    addTeamMember(member_token)
                }
            }
        }
    }

}

class TeamMemberViewHolder(
    context: Context,
    view: View,
    selected: selectedClosure<TeamMemberTable>,
    delegate: ManagerTeamMemberVC
): MyViewHolder2<TeamMemberTable, ManagerTeamMemberVC>(context, view, selected, delegate) {

    override fun bind(row: TeamMemberTable, idx: Int) {

        super.bind(row, idx)

        row.filterRow()
        val no: String = (idx + 1).toString() + "."

        view.findViewById<TextView>(R.id.noTV) ?. let {
            it.text = no
        }

        view.findViewById<TextView>(R.id.nameTV) ?. let {
            it.text = row.memberTable?.nickname
        }

        view.findViewById<TextView>(R.id.dateTV) ?. let {
            it.text = row.created_at.noSec()
        }

        view.findViewById<ImageView>(R.id.deleteIV) ?. let {
            it.setOnClickListener {
                delegate.deleteTeamMember(row)
            }
        }
    }
}