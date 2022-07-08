package com.br.stocks.fragment

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.br.stocks.BuildConfig
import com.br.stocks.R
import com.br.stocks.databinding.FragmentMoreBinding
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.startUpdateFlowForResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMoreBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }

    private fun initView() {
        val string = getString(R.string.about2) + " " + getString(R.string.email)
        binding.description.movementMethod = LinkMovementMethod.getInstance()
        binding.description.setText(addClickablePart(string), TextView.BufferType.SPANNABLE)

        getVersion()
        shareApp()



    }
    private fun shareApp() {
        binding.cvShare.setOnClickListener {
            try {
                var shareMessage =
                    "Hey, checkout this trading app which provide super technical analysis with fundamental details of breakouts stock information with different pattern charts levels with stop loss.\n\n"
                shareMessage =
                    shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                shareIntent.type = "text/plain"
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString()
            }
        }
    }

    private fun getVersion() {
        val manager = requireContext().packageManager
        val info = manager.getPackageInfo(
            requireContext().packageName, 0
        )
        val version = info.versionName
        val versionCode = BuildConfig.VERSION_CODE
        binding.version.text = "$version ($versionCode)"
    }

    private fun addClickablePart(string: String): SpannableStringBuilder {
        val ssb = SpannableStringBuilder(string)
        val idx1 = string.indexOf("trad")
        val idx2 = idx1 + getString(R.string.email).length
//        val clickString = string.substring(idx1, idx2)
        ssb.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                try {
                    val emailUrl =
                        "mailto:tradingtipsstocks@gmail.com?subject=Trading Tips - Stock, Breakout&body="
                    val request = Intent(Intent.ACTION_VIEW)
                    request.data = Uri.parse(emailUrl)
                    startActivity(request)
                } catch (e: ActivityNotFoundException) {
                    Log.d(TAG, "onClick: $e")
                }
            }
        }, idx1, idx2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ssb.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.purple_700)),
            idx1,
            idx2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return ssb
    }

    companion object {
        fun newInstance() = MoreFragment()
    }
}