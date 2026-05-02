package com.app.task_itzon.util

import timber.log.Timber

object LogUtil {
    const val TAG = "ali_"
    fun d(msg: String) {
        Timber.tag(TAG).d(msg)
    }
    fun e( msg: String) {
        Timber.tag(TAG).e(msg)
    }
    fun i( msg: String) {
        Timber.tag(TAG).i(msg)
    }
    fun w(msg: String) {
        Timber.tag(TAG).w(msg)
    }
}
