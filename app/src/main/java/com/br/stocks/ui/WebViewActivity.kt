package com.br.stocks.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.br.stocks.R
import com.br.stocks.databinding.ActivityWebViewBinding
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.utils.isInternetConnected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)

        binding.back.setOnClickListener {
            finish()
        }

        if(isInternetConnected(this)){
            initView()
        }else{
            this.toast(getString(R.string.no_internet))
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {

        val url = intent.getStringExtra("url")

        binding.txtCompanyName.text = intent.getStringExtra("title")

        try {
            if (url != null) {
                binding.webView.settings.loadWithOverviewMode = true
                binding.webView.settings.loadsImagesAutomatically = true
                binding.webView.isVerticalScrollBarEnabled = true
                binding.webView.settings.javaScriptEnabled = true
                binding.webView.settings.domStorageEnabled = true
                binding.webView.webChromeClient = WebChromeClient()
                binding.webView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return false // then it is not handled by default action
                    }

                    override fun onPageStarted(
                        view: WebView?,
                        url: String?,
                        favicon: Bitmap?
                    ) {
                        super.onPageStarted(view, url, favicon)
                        if (view != null) {
                            binding.progressCircular.visibility= ProgressBar.VISIBLE
                        }
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        //hide progressbar here
                        binding.progressCircular.visibility=ProgressBar.GONE
                        
                    }
                }
                binding.webView.loadUrl(url)
            }
        } catch (e: Exception) {
            binding.progressCircular.visibility=ProgressBar.GONE
        }
    }

}
