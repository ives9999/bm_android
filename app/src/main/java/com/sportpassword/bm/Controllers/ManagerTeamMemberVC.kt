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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
import com.sportpassword.bm.databinding.ActivityManagerTeamMemberBinding
import com.sportpassword.bm.databinding.MytablevcBinding
import com.sportpassword.bm.extensions.avatar
import com.sportpassword.bm.member
import java.lang.reflect.Type

open class ManagerTeamMemberVC : BaseActivity(), MyTable2IF {

    private lateinit var binding: ActivityManagerTeamMemberBinding
    private lateinit var view: ViewGroup

    var token: String? = null
    var top: Top? = null
    private var infoTV: TextView? = null

    private val tableType: Type = object : TypeToken<Tables2<TeamMemberTable>>() {}.type
    lateinit var tableView: MyTable2VC<TeamMemberViewHolder, TeamMemberTable, ManagerTeamMemberVC>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManagerTeamMemberBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")!!
        }

        //setMyTitle("球隊隊員")
        findViewById<Top>(R.id.top) ?. let {
            top = it
            it.showPrev(true)
            it.setTitle("參加球隊")
        }

        init()
        refresh()
    }

    override fun init() {

        findViewById<SwipeRefreshLayout>(R.id.refreshSR) ?. let {
            refreshLayout = it
        }

        findViewById<RecyclerView>(R.id.list) ?. let {
            tableView = MyTable2VC(it, refreshLayout, R.layout.team_member_list_cell, ::TeamMemberViewHolder, tableType, this::tableViewSetSelected, this::getDataFromServer, this)
        }

        findViewById<ImageView>(R.id.scanIV) ?. let {
            it.setOnClickListener {
                handleScan()
            }
        }

    }

    override fun refresh() {
        page = 1
        tableView.getDataFromServer(page)
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

    private fun getDataFromServer(page: Int) {
        loadingAnimation.start()
        loading = true

        TeamService.teamMemberList(this, token!!, tableView.page, tableView.perPage) { success ->
            runOnUiThread {
                loadingAnimation.stop()

                //MyTable2IF
                val b: Boolean = showTableView(tableView, TeamService.jsonString)
                if (b) {
                    this.totalPage = tableView.totalPage
                    infoTV?.visibility = View.GONE
                } else {
                    val rootView: ViewGroup = getRootView()
                    infoTV = rootView.setInfo(this, "目前暫無資料")
                }
            }
            //showTableView(myTable, MemberService.jsonString)
        }
    }

    private fun addTeamMember(member_token: String) {
        loadingAnimation.start()
        loading = true

        TeamService.addTeamMember(this, token!!, member_token, member.token!!) { success ->
            runOnUiThread {
                loadingAnimation.stop()
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
            loadingAnimation.start()
            loading = true

            TeamService.deleteTeamMember(this, row.token) { success ->
                runOnUiThread {
                    loadingAnimation.stop()
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
    viewHolder: View,
    delegate: ManagerTeamMemberVC
): MyViewHolder2<TeamMemberTable, ManagerTeamMemberVC>(context, viewHolder, delegate) {

    var noTV: TextView? = null
    var avatarIV: ImageView? = null
    var nameTV: TextView? = null
    var createdTV: TextView? = null
    var deleteIV: ImageView? = null

    init {
        viewHolder.findViewById<TextView>(R.id.noTV) ?. let {
            noTV = it
        }
        viewHolder.findViewById<com.github.siyamed.shapeimageview.CircularImageView>(R.id.avatarIV) ?. let {
            avatarIV = it
        }
        viewHolder.findViewById<TextView>(R.id.nameTV) ?. let {
            nameTV = it
        }
        viewHolder.findViewById<TextView>(R.id.createdATTV) ?. let {
            createdTV = it
        }
        viewHolder.findViewById<ImageView>(R.id.deleteIV) ?. let {
            deleteIV = it
        }
    }

    override fun bind(row: TeamMemberTable, idx: Int) {

        super.bind(row, idx)

        row.filterRow()

        noTV?.text = (idx + 1).toString() + "."

        if (row.memberTable != null && row.memberTable!!.featured_path != null) {
            avatarIV?.avatar(row.memberTable!!.featured_path)
        }

        nameTV?.text = row.memberTable?.nickname

        if (row.created_at != null) {
            createdTV?.text = row.created_at.noSec()
        }

        deleteIV?.setOnClickListener {
            delegate.deleteTeamMember(row)
        }

//        val no: String = (idx + 1).toString() + "."
//
//        view.findViewById<TextView>(R.id.noTV) ?. let {
//            it.text = no
//        }
//
//        view.findViewById<TextView>(R.id.nameTV) ?. let {
//            it.text = row.memberTable?.nickname
//        }

//        view.findViewById<TextView>(R.id.createdATTV) ?. let {
//            it.text = row.created_at.noSec()
//        }

//        view.findViewById<ImageView>(R.id.deleteIV) ?. let {
//            it.setOnClickListener {
//                delegate.deleteTeamMember(row)
//            }
//        }
    }
}