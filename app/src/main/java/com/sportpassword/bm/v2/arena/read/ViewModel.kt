package com.sportpassword.bm.v2.arena.read

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.sportpassword.bm.v2.base.IRepository

class ViewModel(val context: Context, private val repository: IRepository): ViewModel() {

    var readDao: MutableLiveData<ReadDao> = MutableLiveData()
    val isEmpty: LiveData<Boolean> = MutableLiveData(true)

    init {
        getRead()
    }
    private fun getRead() {
        val page: Int = 1
        val perpage: Int = 20
        repository.getRead(context, page, perpage, object : IRepository.IDaoCallback {

            override fun onReadResult(_readDao: ReadDao) {
                readDao.postValue(_readDao)
//                isEmpty = if (readDao.value?.data.rows.isNotEmpty()) {true} else {false}
//                rows.postValue(readDao.data.rows)
            }
        })

    }
}