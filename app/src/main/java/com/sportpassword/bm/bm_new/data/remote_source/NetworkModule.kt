package com.sportpassword.bm.bm_new.data.remote_source


import com.sportpassword.bm.bm_new.data.remote_source.util.HeaderInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.sportpassword.bm.BuildConfig
import com.sportpassword.bm.bm_new.data.remote_source.api.MatchApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder().retryOnConnectionFailure(true)
        builder.addNetworkInterceptor(HeaderInterceptor())
        builder.addNetworkInterceptor(StethoInterceptor())
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
//            logging.level = (HttpLoggingInterceptor.Level.BASIC)
            logging.level = (HttpLoggingInterceptor.Level.BODY)
            builder.addNetworkInterceptor(logging)
        }
        return builder.build()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        return Retrofit.Builder()
            .baseUrl("https://bm.sportpassword.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun provideMatchApi(retrofit: Retrofit): MatchApi = retrofit.create(MatchApi::class.java)
}