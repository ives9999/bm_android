package com.sportpassword.bm.v2.arena.read

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.sportpassword.bm.v2.base.IRepository

class ViewModel(private val context: Context, private val repository: IRepository): ViewModel() {

    var status: MutableLiveData<Int> = MutableLiveData()
    var rows: MutableLiveData<List<ReadDao.Arena>> = MutableLiveData()
    val isEmpty: LiveData<Boolean> = rows.map { it.isEmpty() }

    init {
        getRead()
    }
    private fun getRead() {
        val page: Int = 1
        val perpage: Int = 20
        repository.getRead(context, page, perpage, object : IRepository.IDaoCallback {

            override fun onReadResult(readDao: ReadDao) {
                status.postValue(readDao.status)
                rows.postValue(readDao.data.rows)
            }
        })

    }
}