package com.jesen.biliwebview_module.session

import com.jesen.common_util_lib.datastore.DataStoreUtil

object SessionManager {
    val session: Session by lazy {
        Session(
            DataStoreUtil.readStringData("session_key", ""),
            DataStoreUtil.readStringData("session_value", "")
        )
    }

    fun update(key: String, value: String) {
        session.key = key
        session.value = value
        DataStoreUtil.saveSyncStringData("session_key", key)
        DataStoreUtil.saveSyncStringData("session_value", value)
    }
}