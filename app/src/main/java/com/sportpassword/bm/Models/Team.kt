package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.ID_KEY
import com.sportpassword.bm.Utilities.NICKNAME_KEY

/**
 * Created by ives on 2018/2/14.
 */
class Team {
    var id: Int = 0
        //get() = session.getInt(ID_KEY, 0)
        //set(value) = session.edit().putInt(ID_KEY, value).apply()
    var nickname: String = ""
        //get() = session.getString(NICKNAME_KEY, "")
        //set(value) = session.edit().putString(NICKNAME_KEY, value).apply()

}