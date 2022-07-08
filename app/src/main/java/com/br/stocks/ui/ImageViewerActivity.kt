package com.br.stocks.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.br.stocks.R
import com.br.stocks.databinding.ActivityImageViewerBinding
import com.br.stocks.utils.CancelButton
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_viewer)
        initView()
    }

    private fun initView() {


        val image = intent.extras?.getString("image")
        val images = arrayListOf<String>()
        if (image != null) {
            images.add(image)
        }
        if (images.isNotEmpty()) {
            StfalconImageViewer.Builder(this, images) { view, _ ->
                Glide.with(this@ImageViewerActivity).load(image).error(R.drawable.a1).into(view)
            }.withOverlayView(CancelButton(this@ImageViewerActivity).apply {
                    this.findViewById<ImageView>(R.id.close).setOnClickListener {
                        this@ImageViewerActivity.finish()
                    }
                }).withDismissListener { finish() }.show()
        } else {
            StfalconImageViewer.Builder(this, images) { view, _ ->
                Glide.with(this@ImageViewerActivity).load(R.drawable.a1).into(view)
            }.withOverlayView(CancelButton(this@ImageViewerActivity).apply {
                this.findViewById<ImageView>(R.id.close).setOnClickListener {
                    this@ImageViewerActivity.finish()
                }
            }).withDismissListener { finish() }.show()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}

