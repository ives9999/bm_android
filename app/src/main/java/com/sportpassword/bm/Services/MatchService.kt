package com.sportpassword.bm.Services

import com.sportpassword.bm.Utilities.URL_MATCH_LIST

object MatchService : DataService() {

    override fun getListURL(): String {
        return URL_MATCH_LIST
    }
}