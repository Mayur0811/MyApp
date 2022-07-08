package com.br.stocks.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import com.br.stocks.R

@SuppressLint("ResourceAsColor")
class CancelButton(context: Context): RelativeLayout(context) {
    init { val view = View.inflate(context, R.layout.cancle_button_view, this)
    }
}