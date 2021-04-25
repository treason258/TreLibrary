package com.mjiayou.hellokotlin.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mjiayou.hellokotlin.R
import com.mjiayou.hellokotlin.databinding.FragmentMusicItemBinding
import com.mjiayou.hellokotlin.model.MusicBean
import java.util.*

class MusicAdapter(context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mList: MutableList<MusicBean>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MusicViewHolder(DataBindingUtil.inflate(mLayoutInflater, R.layout.fragment_music_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MusicViewHolder) {
            holder.setData(mList!![position])
        }
    }

    override fun getItemCount(): Int {
        return if (mList != null) mList!!.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun updateData(newList: MutableList<MusicBean>?) {
        if (newList == null) {
            if (mList != null) {
                mList!!.clear()
            } else {
                mList = ArrayList()
            }
        } else {
            mList = newList
        }
    }

    inner class MusicViewHolder(binding: FragmentMusicItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var mBinding: FragmentMusicItemBinding = binding

        fun setData(musicBean: MusicBean) {
            mBinding.tvTitle.text = musicBean.title
            mBinding.tvAuthor.text = musicBean.author
        }
    }
}