package com.mjiayou.hellokotlin.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.mjiayou.hellokotlin.http.RequestSender
import com.mjiayou.hellokotlin.model.result.MovieResponse
import com.mjiayou.hellokotlin.view.MovieAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MovieViewModel(adapter: MovieAdapter) : BaseObservable() {

    var mMovieAdapter: MovieAdapter = adapter
    var mKeyword: ObservableField<String> = ObservableField("movie")
    lateinit var mStatusText: ObservableField<String>
    lateinit var mStatusShow: ObservableField<Int>

    init {
        initView()
        getData()
    }

    private fun initView() {
        mStatusText = ObservableField()
        mStatusText.set("加载中...")
        mStatusShow = ObservableField()
        mStatusShow.set(View.VISIBLE)
    }

    private fun getData() {
        RequestSender
            .provideDoubanApi()
            .request(mKeyword.get(), "热门", 10, 0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MovieResponse> {
                override fun onSubscribe(d: Disposable) {
                    Log.e("treason111", "MovieViewModel | getData | onSubscribe")
                }

                override fun onNext(movieResponse: MovieResponse) {
                    movieResponse.subjects?.size
                    Log.e("treason111", "MovieViewModel | getData | onNext")
                    if (movieResponse.subjects != null && movieResponse.subjects?.size!! > 0) {
                        mStatusText.set("加载成功")
                        mStatusShow.set(View.GONE)

                        mMovieAdapter.updateData(movieResponse.subjects)
                    } else {
                        mStatusText.set("无数据")
                        mStatusShow.set(View.VISIBLE)
                    }
                }

                override fun onError(e: Throwable) {
                    Log.e("treason111", "MovieViewModel | getData | onError | e.message = " + e.message)
                    mStatusText.set("加载失败")
                    mStatusShow.set(View.VISIBLE)

                    e.printStackTrace()
                }

                override fun onComplete() {
                    Log.e("treason111", "MovieViewModel | getData | onComplete")
                }
            })
    }
}