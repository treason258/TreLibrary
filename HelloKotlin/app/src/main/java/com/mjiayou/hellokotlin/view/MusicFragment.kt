package com.mjiayou.hellokotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mjiayou.hellokotlin.R
import com.mjiayou.hellokotlin.model.MusicBean

class MusicFragment : Fragment() {

    lateinit var adapter: MusicAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        val tv_keyword = view.findViewById<TextView>(R.id.tv_keyword)
        val rv_music = view.findViewById<RecyclerView>(R.id.rv_music)

        // tv_keyword
        tv_keyword.text = "五条人"

        // rv_music
        adapter = MusicAdapter(context)
        rv_music.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_music.itemAnimator = DefaultItemAnimator()
        rv_music.adapter = adapter

        val list = ArrayList<MusicBean>()
        list.add(MusicBean("Last Dance", "五条人"))
        list.add(MusicBean("道山靓仔", "五条人"))
        list.add(MusicBean("问题出现在告诉大家", "五条人"))
        list.add(MusicBean("梦幻丽莎发廊", "五条人"))
        list.add(MusicBean("阿珍爱上阿强", "五条人"))
        list.add(MusicBean("初恋", "五条人"))
        list.add(MusicBean("地球仪", "五条人"))
        adapter.updateData(list)
        adapter.notifyDataSetChanged()

        return view
    }
}