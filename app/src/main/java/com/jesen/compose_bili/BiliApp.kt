package com.jesen.compose_bili

import android.app.Application
import com.jesen.common_util_lib.datastore.DataStoreUtil

class BiliApp : Application() {

    companion object {
        private lateinit var app: Application
        val mContext get() = this.app
    }


    override fun onCreate() {
        super.onCreate()
        app = this

        DataStoreUtil.init(this.applicationContext)
    }
}