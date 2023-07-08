package com.sportpassword.bm.bm_new.data.remote_source.util

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Content-Type", "application/json")

        return chain.proceed(builder.build())
    }
}