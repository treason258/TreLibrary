package com.mjiayou.hellokotlin.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.mjiayou.hellokotlin.model.MusicBean
import com.mjiayou.hellokotlin.view.MusicAdapter

class MusicViewModel(adapter: MusicAdapter) : BaseObservable() {

    var mMusicAdapter: MusicAdapter = adapter
    var mKeyword: ObservableField<String> = ObservableField("五条人")

    init {
        initData()
    }

    private fun initData() {
        val list = ArrayList<MusicBean>()
        list.add(MusicBean("Last Dance", "五条人"))
        list.add(MusicBean("道山靓仔", "五条人"))
        list.add(MusicBean("问题出现在告诉大家", "五条人"))
        list.add(MusicBean("梦幻丽莎发廊", "五条人"))
        list.add(MusicBean("阿珍爱上阿强", "五条人"))
        list.add(MusicBean("初恋", "五条人"))
        list.add(MusicBean("地球仪222", "五条人"))
        mMusicAdapter.updateData(list)
        mMusicAdapter.notifyDataSetChanged()
    }
}