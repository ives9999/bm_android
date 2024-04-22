package com.sportpassword.bm.v2.base

import android.content.Context
import com.sportpassword.bm.v2.arena.read.ReadDao

interface IRepository {
    fun getRead(context: Context, page: Int, perpage: Int, callback: IDaoCallback)

    interface IDaoCallback {
        fun onReadResult(readDao: ReadDao)
    }
}