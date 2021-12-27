package com.sportpassword.bm.Models

import com.google.gson.annotations.SerializedName
import com.sportpassword.bm.Utilities.DEGREE
import com.sportpassword.bm.Utilities.STATUS
import com.sportpassword.bm.Utilities.WEEKDAY
import com.sportpassword.bm.Utilities.noSec

class TeamsTable: Tables() {
    var rows: ArrayList<TeamTable> = arrayListOf()
}

class TeamTable: Table() {

    //var leader: String = ""
    var line: String = ""
    var email: String = ""
    var website: String = ""
    var fb: String = ""
    var youtube: String = ""
    var arena_id: Int = -1
    //var arena_name: String = ""
    var play_start: String = ""
    var play_end: String = ""
    var ball: String = ""
    var degree: String = ""
    var charge: String = ""
    var manager_id: Int = -1
    var manager_token: String = ""
    var manager_nickname: String = ""
    var temp_fee_M: Int = -1
    var temp_fee_F: Int = -1
    var people_limit: Int = 0
    var temp_content: String = ""
    var temp_status: String = ""
    var temp_signup_count: Int = 0
    var color: String = ""
    var weekdays: ArrayList<Team_WeekdaysTable> = arrayListOf()
    var arena: ArenaTable? = null
    @SerializedName("signup_date") var signupDate: SignupDateTable? = null

    var isSignup: Boolean = false

    @SerializedName("signup_normal_models") var signupNormalTables: ArrayList<SignupNormalTable> = arrayListOf()
    @SerializedName("signup_standby_models") var signupStandbyTables: ArrayList<SignupStandbyTable> = arrayListOf()

    var play_start_show: String = ""
    var play_end_show: String = ""
    var weekdays_show: String = ""
    var degree_show: String = ""
    var interval_show: String = ""
    var people_limit_show: String = ""
    var temp_signup_count_show: String = ""
    var temp_status_show: String = "上線"
    var temp_fee_M_show: String = ""
    var temp_fee_F_show: String = ""
    var last_signup_date: String = ""

    override fun filterRow() {

        super.filterRow()

        if (play_start != null && play_start.isNotEmpty()) {
            play_start_show = play_start.noSec()
        }

        if (play_end != null && play_end.isNotEmpty()) {
            play_end_show = play_end.noSec()
        }

        if (website == null) {
            website = ""
        }

        if (fb == null) {
            fb = ""
        }

        if (youtube == null) {
            youtube = ""
        }

        if (line == null) {
            line = ""
        }

        if (temp_content == null) {
            temp_content = ""
        }

        if (temp_status != null) {
            temp_status_show = STATUS.from(temp_status).value
            if (temp_status == "online") {
                people_limit_show = "臨打：${people_limit}位"
                temp_signup_count_show = "報名：${temp_signup_count}位"
            } else {
                people_limit_show = "臨打：未開放"
                temp_signup_count_show = ""
            }
        }

        if (weekdays.size > 0) {
            var show: ArrayList<String> = arrayListOf()
            for (weekday in weekdays) {
                val tmp: String = WEEKDAY.intToString(weekday.weekday)
                //val tmp: String = WEEKDAY.from(weekday.weekday).enumToShortString()
                show.add(tmp)
            }
            weekdays_show = show.joinToString(",")
        }

        if (play_start != null && play_end != null && play_start.isNotEmpty() && play_end.isNotEmpty()) {
            interval_show = play_start.noSec() + " ~ " + play_end.noSec()
        }

        if (degree.length > 0) {
            val degrees: Array<String> = degree.split(",").toTypedArray()
            var show: ArrayList<String> = arrayListOf()

            for (value in degrees) {
                val tmp: String = DEGREE.fromEnglish(value).value
                show.add(tmp)
            }
            degree_show = show.joinToString(",")
        }

        if (arena != null) {
            arena!!.filterRow()
        }

        if (signupDate != null) {
            signupDate!!.filterRow()
            last_signup_date = signupDate!!.date
        }
    }
}