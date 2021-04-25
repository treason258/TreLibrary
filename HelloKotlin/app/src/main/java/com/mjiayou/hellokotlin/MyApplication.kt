package com.mjiayou.hellokotlin

import android.app.Application
import androidx.multidex.MultiDex

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}