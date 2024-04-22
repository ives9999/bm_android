package com.sportpassword.bm.v2.arena.read

import android.content.Context
import com.google.gson.Gson
import com.sportpassword.bm.Models.ArenasTable
import com.sportpassword.bm.Services.ArenaService
import com.sportpassword.bm.Utilities.CompletionHandler
import com.sportpassword.bm.Utilities.Global
import com.sportpassword.bm.Utilities.jsonToModels
import com.sportpassword.bm.v2.base.IRepository

class Reposity : IRepository {

    override fun getRead(context: Context, page: Int, perpage: Int, callback: IRepository.IDaoCallback) {
        ArenaService.getList(context, null, null, page, 20) { success ->
            val jsonString = ArenaService.jsonString
            println(jsonString)
            try {
                val t = Gson().fromJson<ReadDao>(jsonString, ReadDao::class.java)
                println(t)
                callback.onReadResult(t)
            } catch (e: java.lang.Exception) {
                println(e.localizedMessage)
            }
        }
    }
}