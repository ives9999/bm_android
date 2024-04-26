package com.sportpassword.bm.v2.arena.read

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sportpassword.bm.v2.base.IRepository

class ViewModel(private val repository: IRepository<ReadDao>): ViewModel() {

    var readDao: MutableLiveData<ReadDao> = MutableLiveData()
    val isEmpty: MutableLiveData<Boolean> = MutableLiveData(false)

    var isToShow: MutableLiveData<Event<Boolean>> = MutableLiveData()


    init {
        getRead()
    }
    private fun getRead() {
        val page: Int = 1
        val perpage: Int = 20
        repository.getRead(page, perpage, null, object : IRepository.IDaoCallback<ReadDao> {

            override fun onSuccess(res: ReadDao) {
                this@ViewModel.readDao.postValue(res)
                isEmpty.value = !(res.status == 200 && res.data.rows.isNotEmpty())
//                rows.postValue(readDao.data.rows)
            }

            override fun onFailure(status: Int, msg: String) {
                println(status)
                println(msg)
            }
        })
    }

    fun toShohw(bool: Boolean) {
        isToShow.value = Event(bool)
    }
}