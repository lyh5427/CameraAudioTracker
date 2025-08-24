package com.yunho.king.presentation.ui.audiointercept

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.yunho.king.GlobalApplication
import com.yunho.king.databinding.ActivityAudioInterceptBinding
import com.yunho.king.presentation.Utils.singleClickListener
import com.yunho.king.presentation.constant.Const
import com.yunho.king.presentation.constant.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AudioInterceptActivity : AppCompatActivity() {
    lateinit var binding: ActivityAudioInterceptBinding
    val viewModel: AudioInterceptViewModel by viewModels()

    lateinit var audioManager: AudioManager
    lateinit var audioRecord: AudioRecord

    private var isAudioAlimCheckBox = false
    private var isAppAlimCheckBox = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioInterceptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        viewModel.packageName = intent.getStringExtra(Const.PKG_NAME)?: ""
        viewModel.getAudioAppData()
        lifecycleScope.launch { setObserver() }
        setAdmobView()
        setListener()

    }

    override fun onResume() {
        super.onResume()
        viewModel.setAppInfo(packageManager)
        startAudio()
    }

    override fun onStop() {
        super.onStop()
        stopAudio()
    }

    private fun setAdmobView() = with(binding) {
        admobView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.e(GlobalApplication.TagName, error.message)
            }
        }
        val adRequest = AdRequest.Builder().build()
        admobView.loadAd(adRequest)
    }

    private suspend fun setObserver() = with(binding){
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.appIcon.collectLatest {
                appImg.setImageDrawable(it)
            }
        }
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.appName.collectLatest {
                when (it.status) {
                    Status.TEXT -> appName.text = it.toString()
                }
            }
        }
    }

    private fun setListener() = with(binding) {
        audioAlimCheckBox.singleClickListener {
            audioAlimCheckBox.isChecked = !isAudioAlimCheckBox
        }

        appAlimCheckBox.singleClickListener {
            appAlimCheckBox.isChecked = !isAppAlimCheckBox
        }

        cancel.singleClickListener {
            setAppAlim()
            finishAffinity()
        }

        btnPopupOk.singleClickListener {
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${viewModel.packageName}"))
            )
        }
    }

    private fun setAppAlim() = with(binding) {
        if (appAlimCheckBox.isChecked) {
            GlobalApplication.prefs!!.appAlim = false
        }

        if (audioAlimCheckBox.isChecked) {
            viewModel.updateNotiFlag()
        }
    }

    private fun startAudio() {
        val sampleRate = 44100
        val bufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            audioRecord.startRecording()
        }
    }

    private fun stopAudio() {
        if (::audioRecord.isInitialized) {
            audioRecord.stop()
        }
    }
}