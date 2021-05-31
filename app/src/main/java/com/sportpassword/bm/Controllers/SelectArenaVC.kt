package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Models.ArenaTable
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Global

class SelectArenaVC : SingleSelectVC1() {

    var arenas1: ArrayList<ArenaTable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var city_id: Int? = null
        if (intent.hasExtra("city_id")) {
            city_id = intent.getIntExtra("city_id", 0)
            if (city_id == 0) {
                city_id = null
            }
        }

        if (city_id != null) {
            TeamService.getArenaByCityID(this, city_id.toInt()) { success ->

                if (success) {
                    arenas1 = TeamService.arenas
                    if (arenas1 != null) {
                        rowsBridge(arenas1!!)
                        notifyChanged()
                    } else {
                        warning("無法取得球館資料，請洽管理員")
                    }
                }
            }
            setMyTitle(Global.zoneIDToName(city_id))
        } else {
            warning("沒有傳送縣市代碼，請洽管理員")
        }
    }

    fun rowsBridge(arenas: ArrayList<ArenaTable>) {

        if (rows.count() > 0) {
            rows.clear()
        } else {
            rows = arrayListOf()
        }
        for(arena in arenas) {
            val name = arena.name
            val id = arena.id
            rows.add(hashMapOf("title" to name, "value" to id.toString()))
        }
    }
}