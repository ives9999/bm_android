package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.sportpassword.bm.Models.ArenaTable
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.ARENA_KEY
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

        key = ARENA_KEY

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

    override fun submit(idx: Int) {

        if (idx >= rows.size) {
            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        }
        val row = rows[idx]
        if (!row.containsKey("value")) {
            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        }

        var cancel: Boolean = false
        if (selected != null && selected!!.isNotEmpty()) {
            if (selected == row["value"]) {
                cancel = true
            }
        }

        //選擇其他的
        if (!cancel) {
            selected = row["value"]
            //dealSelected()
            val intent = Intent()
            intent.putExtra("key", key)
            intent.putExtra("show", row["title"])
            intent.putExtra("selected", selected)
            intent.putExtra("able_type", able_type)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else { //取消原來的選擇
            selected = ""
            generateItems()
            notifyChanged()
        }
    }
}