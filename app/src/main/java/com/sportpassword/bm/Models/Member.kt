package com.sportpassword.bm.Models

/**
 * Created by ivessun on 2018/2/5.
 */
object Member {
    var id: Int = 0
    var nickname: String = ""
    var uid: String = ""
    var name: String = ""
    var channel: String = ""
    var dob: String = ""
    var sex: String = ""
    var tel: String = ""
    var mobile: String = ""
    var email: String = ""
    var pid: String = ""
    var avatar: String = ""
    var type: Int = 0
    var social: String = ""
    var role: MEMBER_ROLE = MEMBER_ROLE.member
    var validate: Int = 0
    var token: String = ""

    var isLoggedIn: Boolean = false
}

enum class MEMBER_ROLE {
    member, sale, designer, manager, admin
}