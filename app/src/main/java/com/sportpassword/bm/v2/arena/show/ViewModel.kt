package com.sportpassword.bm.v2.arena.show

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sportpassword.bm.v2.base.IRepository
import com.sportpassword.bm.v2.base.IShowError
import com.sportpassword.bm.v2.base.IShowLoading
import com.sportpassword.bm.v2.base.ShowError
import com.sportpassword.bm.v2.base.ShowLoading
import com.sportpassword.bm.v2.error.IError

class ViewModel(private val token: String, private val repository: IRepository<ShowDao>, val error: IError):
    ViewModel(),
    IShowLoading by ShowLoading(MutableLiveData(false)),
    IShowError by ShowError(MutableLiveData(false)) {

    val showDao: MutableLiveData<ShowDao> = MutableLiveData()

    init {
        getOne(token)
    }

    private fun getOne(token: String) {
        repository.getOne(token, object : IRepository.IDaoCallback<ShowDao> {
            override fun onSuccess(res: ShowDao) {
                println(res)
                if (res.status != 200) {
                    error.getNetworkNotExist()
                    isShowError.postValue(true)
                } else {
                    this@ViewModel.showDao.postValue(res)
                }
//                isEmpty.postValue(!(res.status == 200 && res.data.rows.isNotEmpty()))
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
}