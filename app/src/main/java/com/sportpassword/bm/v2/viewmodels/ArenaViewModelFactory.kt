package com.sportpassword.bm.v2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sportpassword.bm.v2.repositorys.IRepository

class ArenaViewModelFactory(private val repository: IRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArenaViewModel(repository) as T
    }
}