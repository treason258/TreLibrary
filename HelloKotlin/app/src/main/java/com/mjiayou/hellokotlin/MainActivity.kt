package com.mjiayou.hellokotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mjiayou.hellokotlin.databinding.ActivityMainBinding
import com.mjiayou.hellokotlin.view.MovieFragment

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main) // 通过 DataBindingUtil 获取 ActivityMainBinding

        supportFragmentManager.beginTransaction().add(R.id.fl_container, MovieFragment.getInstance()).commit()
    }
}