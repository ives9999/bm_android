package com.sportpassword.bm.Services

import com.sportpassword.bm.Models.Course

/**
 * Created by ivessun on 2018/3/1.
 */
object CourseService: DataService() {
    override fun setData(id: Int, title: String, featured_path: String, vimeo: String, youtube: String): Course {
        val data = Course(id, title, featured_path, vimeo, youtube)
        return data
    }

}