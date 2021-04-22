package com.sportpassword.bm.Models

class SignupCancelTable: Table() {

    var member_id: Int = -1
    var signupable_id: Int = -1
    var signupable_type: String = ""
    var able_date_id: Int = -1
    var cancel_deadline: String = ""
    var member_name: String = ""
}