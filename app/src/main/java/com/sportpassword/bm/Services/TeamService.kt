package com.sportpassword.bm.Services

import com.sportpassword.bm.Models.Team

/**
 * Created by ives on 2018/2/17.
 */
object TeamService: DataService() {
    override fun setData(id: Int, title: String, featured_path: String, vimeo: String, youtube: String): Team {
        val data = Team(id, title, featured_path, vimeo, youtube)
        return data
    }


}