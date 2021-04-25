package com.mjiayou.hellokotlin.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mjiayou.hellokotlin.R
import com.mjiayou.hellokotlin.databinding.FragmentMovieItemBinding
import com.mjiayou.hellokotlin.model.result.MovieBean
import java.util.*

class MovieAdapter(context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private val mContext: Context? = context
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mList: MutableList<MovieBean>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(DataBindingUtil.inflate(mLayoutInflater, R.layout.fragment_movie_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            holder.setData(mList!![position])
        }
    }

    override fun getItemCount(): Int {
        return if (mList != null) mList!!.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun updateData(newList: List<MovieBean>?) {
        if (newList == null) {
            if (mList != null) {
                mList!!.clear()
            } else {
                mList = ArrayList()
            }
        } else {
            mList = newList as MutableList<MovieBean>?
        }
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(binding: FragmentMovieItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var mBinding: FragmentMovieItemBinding = binding

        fun setData(musicBean: MovieBean) {
            Glide.with(mContext).load(musicBean.cover).into(mBinding.ivCover)
            mBinding.tvTitle.text = musicBean.title
            mBinding.tvRate.text = "评分：" + musicBean.rate
            mBinding.llItem.setOnClickListener(View.OnClickListener {
                val intent = Intent(mContext, WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.EXTRA_URL, musicBean.url)
                mContext!!.startActivity(intent)
            })
        }
    }
}