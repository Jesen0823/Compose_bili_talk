package com.jesen.compose_bili

import android.app.Application

class BiliApp : Application() {

    companion object {
        private lateinit var app: Application
        val mContext get() = this.app
    }


    override fun onCreate() {
        super.onCreate()
        app = this
    }
}