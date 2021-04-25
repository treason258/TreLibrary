package com.mjiayou.hellokotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mjiayou.hellokotlin.R
import com.mjiayou.hellokotlin.databinding.FragmentMovieBinding
import com.mjiayou.hellokotlin.viewmodel.MovieViewModel

class MovieFragment : Fragment() {

    lateinit var mMovieAdapter: MovieAdapter
    lateinit var mBinding: FragmentMovieBinding

    companion object {
        private val INSTANCE = MovieFragment()
        fun getInstance(): MovieFragment {
            return INSTANCE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)

        mMovieAdapter = MovieAdapter(context)

        mBinding.rvMusic.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding.rvMusic.itemAnimator = DefaultItemAnimator()
        mBinding.rvMusic.adapter = mMovieAdapter
        mBinding.viewModel = MovieViewModel(mMovieAdapter)

        return mBinding.root
    }
}