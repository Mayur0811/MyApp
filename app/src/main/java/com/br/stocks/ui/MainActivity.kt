package com.br.stocks.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.br.stocks.R
import com.br.stocks.StocksApplication
import com.br.stocks.databinding.ActivityMainBinding
import com.br.stocks.fragment.CalendarFragment
import com.br.stocks.fragment.HomeFragment
import com.br.stocks.fragment.MoreFragment
import com.br.stocks.fragment.NewsFragment
import com.br.stocks.fragment.NotificationFragment
import com.br.stocks.utils.CommonUtils.toast
import com.br.stocks.utils.Constant
import com.br.stocks.utils.NetworkResult
import com.br.stocks.utils.NotificationService
import com.br.stocks.utils.PreferenceUtil
import com.br.stocks.viewmodel.NetworkViewModel
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var appUpdateManager: AppUpdateManager? = null
    private val UPDATE_REQUEST_CODE = 100
    private lateinit var binding: ActivityMainBinding
    lateinit var networkViewModel: NetworkViewModel
    private var timeBack: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (intent.extras != null) {
            if (intent.extras?.get("post_id") != null) {
                val postId = intent.extras?.getString("post_id")
                val intent = Intent(this@MainActivity, DescriptionActivity::class.java)
                intent.putExtra("post_id", postId)
                startActivity(intent)
            }
        }
        networkViewModel = ViewModelProvider(this)[NetworkViewModel::class.java]
        MobileAds.initialize(this)
        initView()
        inAppUpdate()

        if (!StocksApplication.isAppInBackground)
            startService(Intent(this, NotificationService::class.java))
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.extras != null) {
            if (intent.extras?.get("post_id") != null) {
                val postId = intent.extras?.getString("post_id")
                val passIntent = Intent(this@MainActivity, DescriptionActivity::class.java)
                passIntent.putExtra("post_id", postId)
                startActivity(passIntent)
            }
        }
    }

    private fun initView() {
        getIds()
        if (!isEmulator()) {
            registerDevice()
        }
        showFragment(fragment = HomeFragment())
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    binding.txtTitle.text = getString(R.string.app_name)
                    showFragment(fragment = HomeFragment())
                    true
                }
                R.id.calendar -> {
                    binding.txtTitle.text = getString(R.string.calendar)
                    showFragment(fragment = CalendarFragment())
                    true
                }
                R.id.news -> {
                    binding.txtTitle.text = getString(R.string.news)
                    showFragment(fragment = NewsFragment())
                    true
                }
                R.id.notification -> {
                    binding.txtTitle.text = getString(R.string.notification)
                    showFragment(fragment = NotificationFragment())
                    true
                }
                R.id.more -> {
                    binding.txtTitle.text = getString(R.string.about_us)
                    showFragment(fragment = MoreFragment())
                    true
                }

                else -> false
            }
        }


    }

    private fun inAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo

        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager?.startUpdateFlowForResult(
                    appUpdateInfo, AppUpdateType.IMMEDIATE,
                    this, UPDATE_REQUEST_CODE
                )

            }
        }
        appUpdateInfoTask?.addOnFailureListener {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "inAppUpdate: ${it.message}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
           // Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show()
//            appUpdateManager?.unregisterListener(listener)
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            binding.root,
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager?.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.black))
            show()
        }
    }

    private fun getIds() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val notificationToken = task.result
            PreferenceUtil.stringPreference(Constant.deviceToken).set(notificationToken)

        })
    }

    @SuppressLint("HardwareIds")
    private fun registerDevice() {

        val androidModel = Build.MODEL
        val androidID = Secure.getString(contentResolver, Secure.ANDROID_ID)
        val token = PreferenceUtil.stringPreference(Constant.deviceToken).get()

        val modelName = if (androidModel.contains(" ")) {
            androidModel.replace(" ", "_")
        } else {
            androidModel
        }

        val deviceId = StringBuilder().append(modelName).append("_").append(androidID).toString()

        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(Constant.deviceId, deviceId)
            .addFormDataPart(Constant.deviceToken, token)
            .addFormDataPart(Constant.deviceType, "android")
            .build()

        networkViewModel.registerDevice(requestBody)
        lifecycleScope.launchWhenCreated {
            networkViewModel.registerDevice.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        Log.d("", "device data updated")
                    }
                    is NetworkResult.Error -> {

                    }
                    is NetworkResult.Loading -> {

                    }
                    else -> {}
                }
            }
        }

    }

    private fun defaultFragment() {
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(binding.mainFragmentContainer.id, fragment)
        transaction.commit()
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.mainFragmentContainer.id, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - timeBack > 1000) {
            timeBack = System.currentTimeMillis()
            this.toast("Press Again to Exit")
            return
        }
        super.onBackPressed()
    }


    private fun isEmulator(): Boolean {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.HARDWARE.equals("vbox86")
                || Build.HARDWARE.toLowerCase().contains("nox")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MODEL.toLowerCase().contains("droid4x")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")
                || Build.PRODUCT.toLowerCase().contains("nox")
                || Build.BOARD.toLowerCase().contains("nox")
                || Build.BOOTLOADER.toLowerCase().contains("nox")
                || Build.SERIAL.toLowerCase().contains("nox")
    }
}