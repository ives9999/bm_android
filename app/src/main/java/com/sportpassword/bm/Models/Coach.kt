package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.*

/**
 * Created by ives on 2018/2/23.
 */
class Coach(id: Int, name: String, token: String, featured_path: String="", vimeo: String="", youtube: String=""): SuperData(id, name, token, featured_path, vimeo, youtube) {

    fun  initData() {
        data = mutableMapOf<String, MutableMap<String, Any>>(
                ID_KEY to mutableMapOf("ch" to "編號","vtype" to "Int","value" to -1,"show" to ""),
                NAME_KEY to mutableMapOf("ch" to "姓名","vtype" to "String","value" to "","show" to "","submit" to false),
                CHANNEL_KEY to mutableMapOf("ch" to "頻道","vtype" to "String","value" to "","show" to "","submit" to false),
                WEBSITE_KEY to mutableMapOf("ch" to "網站","vtype" to "String","value" to "","show" to "","submit" to false),
                FB_KEY to mutableMapOf("ch" to "FB","vtype" to "String","value" to "","show" to "","submit" to false),
                YOUTUBE_KEY to mutableMapOf("ch" to "youtube","vtype" to "String","value" to "","show" to "","submit" to false),
                MOBILE_KEY to mutableMapOf("ch" to "行動電話","vtype" to "String","value" to "","show" to "","submit" to false),
                EMAIL_KEY to mutableMapOf("ch" to "email","vtype" to "String","value" to "","show" to "","submit" to false),
                LINE_KEY to mutableMapOf("ch" to "line id","vtype" to "String","value" to "","show" to "","submit" to false),
                SLUG_KEY to mutableMapOf("ch" to "插槽","vtype" to "String","value" to "","show" to "","submit" to false),
                CITY_KEY to mutableMapOf("ch" to "區域","vtype" to "array","value" to 0,"show" to ""),
                COACH_SENIORITY_KEY to mutableMapOf("ch" to "年資","vtype" to "Int","value" to -1,"show" to ""),
                COACH_EXP_KEY to mutableMapOf("ch" to "經歷","vtype" to "String","value" to "","show" to "","submit" to false),
                COACH_FEAT_KEY to mutableMapOf("ch" to "比賽成績","vtype" to "String","value" to "","show" to "","submit" to false),
                COACH_LICENSE_KEY to mutableMapOf("ch" to "證照","vtype" to "String","value" to "","show" to "","submit" to false),
                COACH_CHARGE_KEY to mutableMapOf("ch" to "收費標準","vtype" to "String","value" to "","show" to "","submit" to false),
                CONTENT_KEY to mutableMapOf("ch" to "內容","vtype" to "String","value" to "","show" to "","submit" to false),
                MANAGER_ID_KEY to mutableMapOf("ch" to "","vtype" to "Int","value" to 0,"show" to "","submit" to false),
                SORT_ORDER_KEY to mutableMapOf("ch" to "","vtype" to "Int","value" to 0,"show" to ""),
                PV_KEY to mutableMapOf("ch" to "瀏覽數","vtype" to "Int","value" to 0,"show" to ""),
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