package com.sportpassword.bm.v2.arena.read

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sportpassword.bm.v2.base.Event
import com.sportpassword.bm.v2.base.IRepository
import com.sportpassword.bm.v2.base.IShowError
import com.sportpassword.bm.v2.base.IShowLoading
import com.sportpassword.bm.v2.base.ShowError
import com.sportpassword.bm.v2.base.ShowLoading
import com.sportpassword.bm.v2.error.IError

class ViewModel(private val repository: IRepository<ReadDao>, val error: IError):
    ViewModel(),
    IShowLoading by ShowLoading(MutableLiveData(false)),
    IShowError by ShowError(MutableLiveData(false)) {

    var readDao: MutableLiveData<ReadDao> = MutableLiveData()
    var isEmpty: MutableLiveData<Boolean> = MutableLiveData(false)
//    var isShowError: MutableLiveData<Boolean> = MutableLiveData(false)
//    var isShowLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    var token: MutableLiveData<String> = MutableLiveData()

    init {
        isShowLoading.value = true
        getRead()
    }
    private fun getRead() {
        val page: Int = 1
        val perpage: Int = 20
        repository.getRead(page, perpage, null, object : IRepository.IDaoCallback<ReadDao> {

            override fun onSuccess(res: ReadDao) {
                if (res.status != 200) {
                    error.getNetworkNotExist()
                    isShowError.postValue(true)
                } else {
                    this@ViewModel.readDao.postValue(res)
                }
                isEmpty.postValue(!(res.status == 200 && res.data.rows.isNotEmpty()))
                isShowLoading.postValue(false)
            }

            override fun onFailure(status: Int, msg: String) {
                error.id = status
                error.msg = msg
                isShowError.postValue(true)
//                println(status)
//                println(msg)
                isShowLoading.postValue(false)
            }
        })
    }

    fun toShohw(token: String) {
        this@ViewModel.token.value = token
    }
}