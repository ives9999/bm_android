package com.sportpassword.bm.bm_new.ui.util

import android.content.Context
import android.content.Intent
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.MATCH_GROUP_TOKEN

fun Context.toMatchSignUp(token: String) {
    Intent(this, MatchSignUpActivity::class.java).apply {
        putExtra(MATCH_GROUP_TOKEN, token)
        startActivity(this)
    }
}