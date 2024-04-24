package com.sportpassword.bm.v2.arena

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.sportpassword.bm.v2.base.IRepository

class ArenaViewModelFactory(private val repository: IRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return com.sportpassword.bm.v2.arena.read.ViewModel(repository) as T
    }
}