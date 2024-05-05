package com.sportpassword.bm.v2.arena.show

import androidx.lifecycle.ViewModelProvider
import com.sportpassword.bm.v2.base.IRepository
import com.sportpassword.bm.v2.error.IError

class ViewModelFactory(private val token: String, private val repository: Repository, private val error: IError): ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return ViewModel(token, repository, error) as T
    }
}