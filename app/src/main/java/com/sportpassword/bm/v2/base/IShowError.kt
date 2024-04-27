package com.sportpassword.bm.v2.base

import androidx.lifecycle.MutableLiveData

interface IShowError {
    var isShowError: MutableLiveData<Boolean>
}

class ShowError(override var isShowError: MutableLiveData<Boolean>) : IShowError {

}