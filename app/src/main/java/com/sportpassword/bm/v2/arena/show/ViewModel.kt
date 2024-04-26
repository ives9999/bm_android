package com.sportpassword.bm.v2.arena.show

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sportpassword.bm.v2.arena.read.ReadDao
import com.sportpassword.bm.v2.base.IRepository

class ViewModel(private val repository: IRepository<ReadDao>): ViewModel() {

    val showDao: MutableLiveData<ShowDao> = MutableLiveData()

    init {
        getOne()
    }

    private fun getOne() {

    }
}