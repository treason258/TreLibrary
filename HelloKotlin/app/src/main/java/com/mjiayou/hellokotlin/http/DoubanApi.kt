package com.mjiayou.hellokotlin.http

import com.mjiayou.hellokotlin.model.result.MovieResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface DoubanApi {

    companion object {
        const val HOST = "https://movie.douban.com"
        const val PATH = "j/search_subjects?sort=recommend"
    }

    @GET(PATH)
    fun request(
        @Query("type") type: String?,
        @Query("tag") tag: String,
        @Query("page_limit") page_limit: Int,
        @Query("page_start") page_start: Int
    ): Observable<MovieResponse>
}