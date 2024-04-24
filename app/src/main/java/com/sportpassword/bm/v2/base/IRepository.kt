package com.sportpassword.bm.v2.base

import android.content.Context
import com.sportpassword.bm.v2.arena.read.ReadDao

interface IRepository {
    fun getRead(page: Int, perpage: Int, params: HashMap<String, String>?, callback: IDaoCallback)

    interface IDaoCallback {
        fun onReadResult(readDao: ReadDao)
    }
}