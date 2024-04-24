package com.sportpassword.bm.v2.arena.read

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sportpassword.bm.v2.base.IRepository

class ViewModel(private val repository: IRepository): ViewModel() {

    var readDao: MutableLiveData<ReadDao> = MutableLiveData()
    val isEmpty: MutableLiveData<Boolean> = MutableLiveData(false)


    init {
        getRead()
    }
    private fun getRead() {
        val page: Int = 1
        val perpage: Int = 20
        repository.getRead(page, perpage, null, object : IRepository.IDaoCallback {

            override fun onReadResult(readDao: ReadDao) {
                this@ViewModel.readDao.postValue(readDao)
                isEmpty.value = !(readDao.status == 200 && readDao.data.rows.count() > 0)
//                rows.postValue(readDao.data.rows)
            }
        })

    }
}