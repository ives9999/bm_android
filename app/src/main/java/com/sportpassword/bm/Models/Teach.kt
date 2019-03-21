package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.*

/**
 * Created by ivessun on 2018/3/1.
 */
class Teach(id: Int, name: String, token: String, featured_path: String="", vimeo: String="", youtube: String=""): SuperData(id, name, token, featured_path, vimeo, youtube) {
    fun initData() {
        data = mutableMapOf<String, MutableMap<String, Any>>(
            ID_KEY to mutableMapOf("ch" to "編號","vtype" to "Int","value" to -1,"show" to ""),
        TITLE_KEY to mutableMapOf("ch" to "標題","vtype" to "String","value" to "","show" to "","submit" to false),
        CHANNEL_KEY to mutableMapOf("ch" to "頻道","vtype" to "String","value" to "","show" to "","submit" to false),
        CONTENT_KEY to mutableMapOf("ch" to "內容","vtype" to "String","value" to "","show" to "","submit" to false),
        SLUG_KEY to mutableMapOf("ch" to "插槽","vtype" to "String","value" to "","show" to "","submit" to false),
        VIMEO_KEY to mutableMapOf("ch" to "vimeo","vtype" to "String","value" to "","show" to "","submit" to false),
        YOUTUBE_KEY to mutableMapOf("ch" to "youtube","vtype" to "String","value" to "","show" to "","submit" to false),
        PV_KEY to mutableMapOf("ch" to "瀏覽數","vtype" to "Int","value" to 0,"show" to ""),
        COURSE_PROVIDER_KEY to mutableMapOf("ch" to "出處","vtype" to "String","value" to "","show" to "","submit" to false),
        SORT_ORDER_KEY to mutableMapOf("ch" to "","vtype" to "Int","value" to 0,"show" to ""),
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