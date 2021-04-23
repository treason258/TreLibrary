package com.mjiayou.hellokotlin.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mjiayou.hellokotlin.R
import com.mjiayou.hellokotlin.model.MusicBean
import java.util.*

class MusicAdapter(mContext: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mList: MutableList<MusicBean>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MusicViewHolder(mLayoutInflater.inflate(R.layout.fragment_music_item, parent, false))
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

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tv_title: TextView = itemView.findViewById(R.id.tv_title)
        private val tv_author: TextView = itemView.findViewById(R.id.tv_author)

        fun setData(musicBean: MusicBean) {
            tv_title.text = musicBean.title
            tv_author.text = musicBean.author
        }
    }
}