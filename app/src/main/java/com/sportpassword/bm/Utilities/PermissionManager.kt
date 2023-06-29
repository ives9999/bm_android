package com.sportpassword.bm.Utilities

import androidx.appcompat.app.AppCompatActivity

class PermissionManager constructor(private val activity: AppCompatActivity) {

    companion object {
        fun from(activity: AppCompatActivity) = PermissionManager(activity)
    }
}