package com.mjiayou.hellokotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mjiayou.hellokotlin.view.MusicFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.fl_container, MusicFragment()).commit()
    }
}