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
import com.mjiayou.hellokotlin.databinding.FragmentMusicBinding
import com.mjiayou.hellokotlin.viewmodel.MusicViewModel

class MusicFragment : Fragment() {

    lateinit var mMusicAdapter: MusicAdapter
    lateinit var mBinding: FragmentMusicBinding

    companion object {
        private val INSTANCE = MusicFragment()
        fun getInstance(): MusicFragment {
            return INSTANCE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_music, container, false)

        mMusicAdapter = MusicAdapter(context)

        mBinding.rvMusic.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding.rvMusic.itemAnimator = DefaultItemAnimator()
        mBinding.rvMusic.adapter = mMusicAdapter
        mBinding.viewModel = MusicViewModel(mMusicAdapter)

        return mBinding.root
    }
}