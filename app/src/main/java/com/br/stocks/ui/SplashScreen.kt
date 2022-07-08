package com.br.stocks.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.br.stocks.R
import com.br.stocks.StocksApplication
import com.br.stocks.databinding.ActivitySplashScreenBinding
import com.br.stocks.utils.Constant
import com.br.stocks.utils.NotificationService
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        initView()
    }

    @SuppressLint("WrongConstant")
    private fun initView() {

        Timer().schedule(object : TimerTask() {
            override fun run() {
                val sh: SharedPreferences =
                    getSharedPreferences(Constant.MyPREFERENCES, MODE_APPEND)
                var appName = sh.getString(getString(R.string.app_name), null)

                if (intent.extras != null) {
                    if (intent.extras?.get("post_id") != null) {
                        val postId = intent.extras?.getString("post_id")
                        val intent = Intent(this@SplashScreen, MainActivity::class.java)
                        intent.putExtra("post_id", postId)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@SplashScreen, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 2000)
    }
}