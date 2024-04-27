package com.sportpassword.bm.v2.arena.read

import androidx.lifecycle.ViewModelProvider
import com.sportpassword.bm.v2.base.IRepository
import com.sportpassword.bm.v2.error.IError

class ViewModelFactory(private val repository: IRepository<ReadDao>, val error: IError): ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return ViewModel(repository, error) as T
    }
}