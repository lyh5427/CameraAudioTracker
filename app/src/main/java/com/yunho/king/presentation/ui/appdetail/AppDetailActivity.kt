package com.yunho.king.presentation.ui.appdetail

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yunho.king.Const
import com.yunho.king.R
import com.yunho.king.Status
import com.yunho.king.Utils.Util
import com.yunho.king.Utils.singleClickListener
import com.yunho.king.databinding.ActivityAppDetailBinding
import com.yunho.king.presentation.ui.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AppDetailActivity : BaseActivity() {

    lateinit var binding: ActivityAppDetailBinding
    val viewModel: AppDetailViewModel by viewModels()

    lateinit var pkgName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppDetailBinding.inflate(layoutInflater)

        pkgName = intent.getStringExtra((Const.PKG_NAME))?: ""

        lifecycleScope.launch { setObserver() }

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setView()

        binding.appName.singleClickListener {
            setView()
        }
    }

    private suspend fun setObserver() = with(binding) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.appName.collect {
                    when (it.status) {
                        Status.TEXT -> appName.text = it.msg
                    }
                }
            }

            launch {
                viewModel.downLoad.collect {
                    when (it.status) {
                        Status.TEXT -> {
                            appDownLocation.text = getString(
                                    R.string.app_detail_download,
                                    getInstallPackageName(it.msg, this@AppDetailActivity))

                            appIcon.setImageDrawable(
                                Util.getAppIcon(it.msg, this@AppDetailActivity))
                        }
                    }
                }
            }

            launch {
                viewModel.cameraState.collect {
                    when (it.status) {
                        Status.TEXT -> {
                            val text = if (it.msg.toBoolean()) {
                                getString(R.string.state_allow)
                            } else {
                                getString(R.string.state_denied)
                            }
                            cameraStateTitle.text = getString(R.string.perm_usage_state_title, text)
                        }
                    }
                }
            }

            launch {
                viewModel.cameraCount.collect {
                    when (it.status) {
                        Status.TEXT -> {
                            val text = getString(R.string.perm_usage_count_title, it.msg)
                            cameraCountTitle.text = text
                        }
                    }
                }
            }

            launch {
                viewModel.cameraLastUseDate.collect {
                    when (it.status) {
                        Status.TEXT -> {
                            val text = getString(
                                R.string.perm_usage_last_date_title,
                                Util.getDate(it.msg.toLong())
                            )

                            cameraLastUseDate.text = text
                        }
                    }
                }
            }

            launch {
                viewModel.audioState.collect {
                    when (it.status) {
                        Status.TEXT -> {
                            val text = if (it.msg.toBoolean()) {
                                getString(R.string.state_allow)
                            } else {
                                getString(R.string.state_denied)
                            }
                            audioStateTitle.text = getString(R.string.perm_usage_state_title, text)
                        }
                    }
                }
            }

            launch {
                viewModel.audioCount.collect {
                    when (it.status) {
                        Status.TEXT -> {
                            val text = getString(R.string.perm_usage_count_title, it.msg)
                            audioCountTitle.text = text
                        }
                    }
                }
            }

            launch {
                viewModel.audioLastUseDate.collect {
                    when (it.status) {
                        Status.TEXT -> {
                            val text = getString(
                                R.string.perm_usage_last_date_title,
                                Util.getDate(it.msg.toLong())
                            )

                            audioLastUseDate.text = text
                        }
                    }
                }
            }
        }
    }

    fun setView() = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getAppData(pkgName)
        }
    }
}


