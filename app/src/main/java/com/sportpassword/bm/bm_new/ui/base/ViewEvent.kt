package com.sportpassword.bm.bm_new.ui.base

sealed class ViewEvent {
    object Loading : ViewEvent()
    object Done : ViewEvent()
    object EmptyData : ViewEvent()

    open class ViewEventData<T>(val data: T? = null) : ViewEvent()
    abstract class CustomViewEvent() : ViewEvent()
    data class SignOut(val message: String? = null) : ViewEvent()
    data class UnknownError(val error: Throwable?) : ViewEvent()
    data class NormalError(val msg: String?) : ViewEvent()
    class ServerError() : CustomViewEvent()
}

