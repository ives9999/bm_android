package com.sportpassword.bm.Models

import com.google.gson.annotations.SerializedName

class TeamMemberTable: Table() {
    var team_id: Int = -1
    var member_id: Int = -1

    @SerializedName("member") var memberTable: MemberTable? = null
    @SerializedName("manager") var managerTable: MemberTable? = null
    @SerializedName("team") var teamTable: TeamTable? = null

    var isLeave: Boolean = false
    var leaveTime: String = ""

    override fun filterRow() {
        super.filterRow()

        memberTable?.filterRow()
        managerTable?.filterRow()
        teamTable?.filterRow()
    }
}