package com.sportpassword.bm.v2.arena.read

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sportpassword.bm.Utilities.PERPAGE
import com.sportpassword.bm.v2.base.Event
import com.sportpassword.bm.v2.base.IRepository
import com.sportpassword.bm.v2.base.IShowError
import com.sportpassword.bm.v2.base.IShowLoading
import com.sportpassword.bm.v2.base.ShowError
import com.sportpassword.bm.v2.base.ShowLoading
import com.sportpassword.bm.v2.error.IError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

//class ViewModel(private val repository: IRepository<ReadDao>, val error: IError):
class ViewModel(private val repository: Repository):
    ViewModel() {

//    var readDao: MutableLiveData<ReadDao> = MutableLiveData()
//    var isEmpty: MutableLiveData<Boolean> = MutableLiveData(false)

      // move to IShowError
//    var isShowError: MutableLiveData<Boolean> = MutableLiveData(false)
      // move to IShowLoading
//    var isShowLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    var token: MutableLiveData<String> = MutableLiveData()

//    init {
//        isShowLoading.value = true
//        getRead()
//    }

    fun getRead(): Flow<PagingData<PageArena>> {
        val flow = repository.getRead2()
        return flow.cachedIn(viewModelScope)
    }
//    private fun getRead() {
//        viewModelScope.launch {
//            repository.getRead2(1, PERPAGE, null)
//                .catch {
//                    println(it)
//                    error.msg = it.message.toString()
//                    isShowError.postValue(true)
//                    isShowLoading.postValue(false)
//                }
//                .collect {
//                    println(it)
//                    if (it.status != 200) {
//                        error.getNetworkNotExist()
//                        isShowError.postValue(true)
//                    } else {
//                        readDao.postValue(it)
//                    }
//                    isShowLoading.postValue(false)
//                }
//        }
//    }

//        val page: Int = 1
//        val perpage: Int = 20
//        repository.getRead(page, perpage, null, object : IRepository.IDaoCallback<ReadDao> {
//
//            override fun onSuccess(res: ReadDao) {
//                if (res.status != 200) {
//                    error.getNetworkNotExist()
//                    isShowError.postValue(true)
//                } else {
//                    this@ViewModel.readDao.postValue(res)
//                }
//                isEmpty.postValue(!(res.status == 200 && res.data.rows.isNotEmpty()))
//                isShowLoading.postValue(false)
//            }
//
//            override fun onFailure(status: Int, msg: String) {
//                error.id = status
//                error.msg = msg
//                isShowError.postValue(true)
////                println(status)
////                println(msg)
//                isShowLoading.postValue(false)
//            }
//        })
//    }

    fun toShow(token: String) {
        this@ViewModel.token.value = token
    }
}