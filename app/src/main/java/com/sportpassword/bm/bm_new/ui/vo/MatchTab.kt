package com.sportpassword.bm.bm_new.ui.vo

import androidx.annotation.StringRes
import com.sportpassword.bm.R

enum class MatchTab(@StringRes val titleResId: Int) {
    CONTENT(R.string.match_content),
    BROCHURE(R.string.match_brochure),
    SIGNUP(R.string.match_sign_up)
}