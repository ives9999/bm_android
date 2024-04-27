package com.sportpassword.bm.v2.base

import androidx.lifecycle.MutableLiveData

interface IShowLoading {
    var isShowLoading: MutableLiveData<Boolean>
}

class ShowLoading(override var isShowLoading: MutableLiveData<Boolean>) : IShowLoading {

}