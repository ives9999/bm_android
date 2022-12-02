package com.sportpassword.bm.Models

class TeamMemberTable: Table() {
    var team_id: Int = -1
    var member_id: Int = -1
    var member_nickname: String = ""
    var manager_nickname: String = ""
    var team_name: String = ""

    override fun filterRow() {
        super.filterRow()
    }
}