package com.sportpassword.bm.Models

class SignupDateTable: Table() {

    var success: Boolean = true
    var isSignup: Boolean = false
    var cancel: Boolean = false
    var date: String = ""
    var deadline: String = ""
    //var standby: Boolean = true
    var isStandby: Boolean = true
    var canSignup: Boolean = false
    var msg: String = ""
}