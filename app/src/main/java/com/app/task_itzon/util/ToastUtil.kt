package com.app.task_itzon.util

import android.content.Context
import android.widget.Toast

class ToastUtil(private val context: Context){

    fun showShort( message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun showLong( message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }
}