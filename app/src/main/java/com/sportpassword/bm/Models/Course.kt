package com.sportpassword.bm.Models

/**
 * Created by ivessun on 2018/3/1.
 */
class Course(id: Int, name: String, token: String, featured_path: String="", vimeo: String="", youtube: String=""): SuperData(id, name, token, featured_path, vimeo, youtube) {
}