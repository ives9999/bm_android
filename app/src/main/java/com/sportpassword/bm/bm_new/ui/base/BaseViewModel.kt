package com.sportpassword.bm.bm_new.ui.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException

open class BaseViewModel(private val state: SavedStateHandle) : ViewModel(), KoinComponent {
    protected val eventChannel = Channel<ViewEvent>(Channel.BUFFERED)
    val eventFlow = eventChannel.receiveAsFlow()
    val gson: Gson by inject()


    private fun handleErrors(e: Throwable) {
        println("********* handleErrors $e")
    }

    fun <T> Flow<T>.setupBase(
        isOver: Boolean = true,
        needLoading: Boolean = true,
        onFail: (suspend (Throwable?) -> Unit)? = null
    ): Flow<T> {
        return this.onStart {
            if (needLoading) eventChannel.send(ViewEvent.Loading)
        }.retryWhen { e, attempt ->
            false
        }.catch { e ->
            when (e) {
                is HttpException -> {
                    with(e.response()?.errorBody()?.string()) {
                        if (this?.isJSONValid() == true) {
                            eventChannel.send(ViewEvent.NormalError("error code: ${e.code()}, $this"))
                        } else {
                            eventChannel.send(ViewEvent.ServerError())
                        }
                        onFail?.invoke(e)
                    }
                }
                else -> {  //無網路或其它錯誤時
                    eventChannel.send(ViewEvent.UnknownError(e))
                }
            }
            handleErrors(e)
            e.printStackTrace()
        }
            .onCompletion {
                if (isOver) eventChannel.send(ViewEvent.Done)
            }
    }

    private fun String.isJSONValid(): Boolean {
        try {
            JSONObject(this)
        } catch (ex: JSONException) {
            try {
                JSONArray(this)
            } catch (ex1: JSONException) {
                return false
            }
        }
        return true
    }
}
