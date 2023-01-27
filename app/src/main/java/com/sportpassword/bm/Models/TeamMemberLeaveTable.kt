package com.sportpassword.bm.Models

class TeamMemberLeaveTable: Table() {

    var team_member_id: Int = -1
    var play_date: String = ""

    override fun filterRow() {
        super.filterRow()
    }
}