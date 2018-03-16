package com.sportpassword.bm.Models

/**
 * Created by ives on 2018/2/17.
 */
open class Data(val id: Int, val title: String, val token: String, val featured_path: String="", val vimeo: String="", val youtube: String="") {
    open var data: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()

    open fun dataReset(){}
}