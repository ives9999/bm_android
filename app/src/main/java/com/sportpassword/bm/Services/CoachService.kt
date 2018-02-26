package com.sportpassword.bm.Services

import com.sportpassword.bm.Models.Coach

/**
 * Created by ives on 2018/2/23.
 */
object CoachService: DataService() {

    override fun setData(id: Int, title: String, featured_path: String): Coach {
        val data = Coach(id, title, featured_path)
        return data
    }
}