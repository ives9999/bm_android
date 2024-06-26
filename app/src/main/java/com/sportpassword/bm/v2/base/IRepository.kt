package com.sportpassword.bm.v2.base

import android.content.Context
import com.sportpassword.bm.Utilities.PERPAGE
import com.sportpassword.bm.v2.arena.read.ReadDao

interface IRepository<T> {

    fun getRead(page: Int = 1, perpage: Int = PERPAGE, otherParams: Map<String, String>?, callback: IDaoCallback<T>)
    fun getOne(token: String, callback: IDaoCallback<T>)

    interface IDaoCallback<T> {
        fun onSuccess(res: T)
        fun onFailure(status: Int, msg: String)
    }
}