package com.sportpassword.bm.v2.error

interface IError {
    var id: Int
    var msg: String

    fun getNetworkNotExist()
}