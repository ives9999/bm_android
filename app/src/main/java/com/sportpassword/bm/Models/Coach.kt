package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member

/**
 * Created by ives on 2018/2/23.
 */
class Coach(id: Int, name: String, token: String, featured_path: String="", vimeo: String="", youtube: String=""): SuperData(id, name, token, featured_path, vimeo, youtube) {

    override var sections: ArrayList<String> = arrayListOf("", "聯絡資訊", "展示資訊", "地區與收費", "其他說明")
    override var rows: ArrayList<ArrayList<String>> = arrayListOf(
            arrayListOf(NAME_KEY),
            arrayListOf(MOBILE_KEY,EMAIL_KEY, FB_KEY, LINE_KEY),
            arrayListOf(WEBSITE_KEY, YOUTUBE_KEY),
            arrayListOf(COACH_SENIORITY_KEY, CITY_KEY),
            arrayListOf(CHARGE_KEY,COACH_LICENSE_KEY, COACH_EXP_KEY,COACH_FEAT_KEY,CONTENT_KEY)
    )

    fun  initData() {
        data = mutableMapOf<String, MutableMap<String, Any>>(
                NAME_KEY to mutableMapOf("ch" to "姓名","vtype" to "String","value" to "","show" to "","submit" to true,"atype" to none,"text_field" to true,"keyboardType" to defaultPad),
                MOBILE_KEY to mutableMapOf("ch" to "行動電話","vtype" to "String","value" to "","show" to "","submit" to true,"atype" to none,"text_field" to true,"keyboardType" to numberPad),
                EMAIL_KEY to mutableMapOf("ch" to "email","vtype" to "String","value" to "","show" to "","submit" to true,"atype" to none,"text_field" to true,"keyboardType" to emailPad),
                FB_KEY to mutableMapOf("ch" to "FB","vtype" to "String","value" to "","show" to "","submit" to true,"atype" to none,"text_field" to true,"keyboardType" to defaultPad),
                LINE_KEY to mutableMapOf("ch" to "line id","vtype" to "String","value" to "","show" to "","submit" to true,"atype" to none,"text_field" to true,"keyboardType" to defaultPad),
                WEBSITE_KEY to mutableMapOf("ch" to "網站","vtype" to "String","value" to "","show" to "","submit" to true,"atype" to none,"text_field" to true,"keyboardType" to defaultPad),
                YOUTUBE_KEY to mutableMapOf("ch" to "youtube","vtype" to "String","value" to "","show" to "","submit" to true,"atype" to none,"text_field" to true,"keyboardType" to defaultPad),
                COACH_SENIORITY_KEY to mutableMapOf("ch" to "年資","vtype" to "Int","value" to -1,"show" to "","submit" to true,"atype" to none,"text_field" to true,"keyboardType" to numberPad),
                CITY_KEY to mutableMapOf("ch" to "區域","vtype" to "array","value" to 0,"show" to "","submit" to true,"atype" to more),
                COACH_EXP_KEY to mutableMapOf("ch" to "經歷","vtype" to "String","value" to "","show" to "","submit" to true,"atype" to more),
                COACH_FEAT_KEY to mutableMapOf("ch" to "比賽成績","vtype" to "String","value" to "","show" to "","submit" to true,"atype" to more),
                COACH_LICENSE_KEY to mutableMapOf("ch" to "證照","vtype" to "String","value" to "","show" to "","submit" to true,"atype" to more),
                COACH_CHARGE_KEY to mutableMapOf("ch" to "收費標準","vtype" to "String","value" to "","show" to "","submit" to true,"atype" to more),
                CONTENT_KEY to mutableMapOf("ch" to "內容","vtype" to "String","value" to "","show" to "","submit" to true),

                ID_KEY to mutableMapOf("ch" to "編號","vtype" to "Int","value" to -1,"show" to ""),
                CHANNEL_KEY to mutableMapOf("ch" to "頻道","vtype" to "String","value" to "","show" to "","submit" to false),
                SLUG_KEY to mutableMapOf("ch" to "插槽","vtype" to "String","value" to "","show" to "","submit" to false),
                MANAGER_ID_KEY to mutableMapOf("ch" to "","vtype" to "Int","value" to 0,"show" to "","submit" to false),
                SORT_ORDER_KEY to mutableMapOf("ch" to "","vtype" to "Int","value" to 0,"show" to "","submit" to false),
                PV_KEY to mutableMapOf("ch" to "瀏覽數","vtype" to "Int","value" to 0,"show" to "","submit" to false),
                COLOR_KEY to mutableMapOf("ch" to "","vtype" to "String","value" to "","show" to "","submit" to false),
                STATUS_KEY to mutableMapOf("ch" to "狀態","vtype" to "String","value" to "","show" to "","submit" to false),
                TOKEN_KEY to mutableMapOf("ch" to "","vtype" to "String","value" to "","show" to "","submit" to false),
                CREATED_ID_KEY to mutableMapOf("ch" to "建立者","vtype" to "Int","value" to 0,"show" to "","submit" to false),
                CREATED_AT_KEY to mutableMapOf("ch" to "建立時間","vtype" to "String","value" to "","show" to "","submit" to false),
                UPDATED_AT_KEY to mutableMapOf("ch" to "最後一次修改時間","vtype" to "String","value" to "","show" to "","submit" to false)
        )
        for ((key, item) in data) {
            //data[key]!!["show"] = "未提供"
            data[key]!!["change"] = false
            data[key]!!["key"] = key
        }
    }

    override fun dataReset() {
        initData()
    }
}