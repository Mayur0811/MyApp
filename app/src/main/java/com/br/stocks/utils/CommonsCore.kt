package com.br.stocks.utils

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object CommonsCore {
    var context: Context? = null
        private set

    fun init(context: Context?) {
        if (CommonsCore.context == null) {
            CommonsCore.context = context
        }
    }
}