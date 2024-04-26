package com.sportpassword.bm.v2.error

class Error(override var id: Int, override var msg: String) : IError {

    val NETWORKNOTEXIST: Int = 99;     // 沒有網路連線
    val TOKENINVALID: Int = 100;       // token不存在

    override fun getNetworkNotExist() {
        id = NETWORKNOTEXIST
        msg = "沒有網路連線"
    }


}