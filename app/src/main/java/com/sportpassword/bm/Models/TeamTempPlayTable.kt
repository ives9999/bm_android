package com.sportpassword.bm.Models

class TeamTempPlayTable: Table() {

    var team_id: Int = 0
    var member_id: Int = 0
    var play_date: String = ""
    var memberTable: MemberTable? = null
}