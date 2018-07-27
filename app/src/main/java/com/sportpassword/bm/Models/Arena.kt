package com.sportpassword.bm.Models

/**
 * Created by ives on 2018/3/4.
 */
import com.sportpassword.bm.Utilities.*

class Arena(id: Int, name: String, token: String="", featured_path: String=""): SuperData(id, name, token, featured_path) {

    fun initData() {
        data = mutableMapOf<String, MutableMap<String, Any>>(
                ID_KEY to mutableMapOf("ch" to "編號","vtype" to "Int","value" to -1,"show" to ""),
                NAME_KEY to mutableMapOf("ch" to "姓名","vtype" to "String","value" to "","show" to "","submit" to false),
                CHANNEL_KEY to mutableMapOf("ch" to "頻道","vtype" to "String","value" to "","show" to "","submit" to false),
                TEL_KEY to mutableMapOf("ch" to "電話","vtype" to "String","value" to "","show" to "","submit" to false),
                FB_KEY to mutableMapOf("ch" to "FB","vtype" to "String","value" to "","show" to "","submit" to false),
                WEBSITE_KEY to mutableMapOf("ch" to "網站","vtype" to "String","value" to "","show" to "","submit" to false),
                EMAIL_KEY to mutableMapOf("ch" to "email","vtype" to "String","value" to "","show" to "","submit" to false),
                ARENA_OPEN_TIME_KEY to mutableMapOf("ch" to "營業開始時間","vtype" to "String","value" to "","show" to "","submit" to false),
                ARENA_CLOSE_TIME_KEY to mutableMapOf("ch" to "營業結束時間","vtype" to "String","value" to "","show" to "","submit" to false),
                ARENA_BLOCK_KEY to mutableMapOf("ch" to "場地","vtype" to "Int","value" to 0,"show" to "","submit" to false),
                CITY_KEY to mutableMapOf("ch" to "縣市","vtype" to "array","value" to 0,"show" to ""),
                AREA_KEY to mutableMapOf("ch" to "區域","vtype" to "array","value" to 0,"show" to ""),
                ROAD_KEY to mutableMapOf("ch" to "路名","vtype" to "String","value" to "","show" to "","submit" to false),
                ZIP_KEY to mutableMapOf("ch" to "郵遞區號","vtype" to "String","value" to "","show" to "","submit" to false),
                ARENA_AIR_CONDITION_KEY to mutableMapOf("ch" to "冷氣","vtype" to "Boolean","value" to false,"show" to "","submit" to false),
                ARENA_PARKING_KEY to mutableMapOf("ch" to "停車位","vtype" to "Int","value" to 0,"show" to "","submit" to false),
                ARENA_BATHROOM_KEY to mutableMapOf("ch" to "浴室","vtype" to "Int","value" to 0,"show" to "","submit" to false),
                ARENA_CHARGE_KEY to mutableMapOf("ch" to "收費標準","vtype" to "String","value" to "","show" to "","submit" to false),
                CONTENT_KEY to mutableMapOf("ch" to "內容","vtype" to "String","value" to "","show" to "","submit" to false),
                MANAGER_ID_KEY to mutableMapOf("ch" to "","vtype" to "Int","value" to 0,"show" to "","submit" to false),
                SLUG_KEY to mutableMapOf("ch" to "插槽","vtype" to "String","value" to "","show" to "","submit" to false),
                PV_KEY to mutableMapOf("ch" to "瀏覽數","vtype" to "Int","value" to 0,"show" to ""),
                SORT_ORDER_KEY to mutableMapOf("ch" to "","vtype" to "Int","value" to 0,"show" to ""),
                COLOR_KEY to mutableMapOf("ch" to "","vtype" to "String","value" to "","show" to "","submit" to false),
                STATUS_KEY to mutableMapOf("ch" to "狀態","vtype" to "String","value" to "","show" to "","submit" to false),
                TOKEN_KEY to mutableMapOf("ch" to "","vtype" to "String","value" to "","show" to "","submit" to false),
                CREATED_ID_KEY to mutableMapOf("ch" to "建立者","vtype" to "Int","value" to 0,"show" to ""),
                CREATED_AT_KEY to mutableMapOf("ch" to "建立時間","vtype" to "String","value" to "","show" to "","submit" to false),
                UPDATED_AT_KEY to mutableMapOf("ch" to "最後一次修改時間","vtype" to "String","value" to "","show" to "","submit" to false)
        )
        for ((key, item) in data) {
            data[key]!!["show"] = "未提供"
        }
    }
    override fun dataReset() {
        initData()
    }

}