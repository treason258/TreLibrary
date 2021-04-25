package com.mjiayou.hellokotlin.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap

object RequestSender {

    private val mApiMap: MutableMap<String, Any?> = ConcurrentHashMap()

    fun provideDoubanApi(): DoubanApi {
        val doubanApi: DoubanApi
        if (mApiMap.containsKey(DoubanApi.HOST) && mApiMap[DoubanApi.HOST] != null) {
            doubanApi = mApiMap[DoubanApi.HOST] as DoubanApi
        } else {
            doubanApi = createDoubanApi()
            mApiMap[DoubanApi.HOST] = doubanApi
        }
        return doubanApi
    }

    fun createDoubanApi(): DoubanApi {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request()
                    .newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
        return Retrofit.Builder()
            .baseUrl(DoubanApi.HOST)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(DoubanApi::class.java)
    }
}