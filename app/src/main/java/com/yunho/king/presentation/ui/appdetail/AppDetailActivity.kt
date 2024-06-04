package com.yunho.king.presentation.ui.appdetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withResumed
import com.yunho.king.Const
import com.yunho.king.R
import com.yunho.king.Status
import com.yunho.king.Utils.Util
import com.yunho.king.databinding.ActivityAppDetailBinding
import com.yunho.king.presentation.ui.base.BaseActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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
    }

    suspend fun setObserver() = with(binding) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.appName.collect {
                when (it.status) {
                    Status.TEXT -> appName.text = it.msg
                }
            }
        }

        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.downLoad.collect {
                when (it.status) {
                    Status.TEXT -> appDownLocation.text = it.msg
                }
            }
        }

        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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

        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.cameraCount.collect {
                when (it.status) {
                    Status.TEXT -> {
                        val text = getString(R.string.perm_usage_count_title, it.msg)
                        cameraCountTitle.text = text
                    }
                }
            }
        }

        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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


        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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

        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.audioCount.collect {
                when (it.status) {
                    Status.TEXT -> {
                        val text = getString(R.string.perm_usage_count_title, it.msg)
                        audioCountTitle.text = text
                    }
                }
            }
        }

        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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

    fun setView() = with(binding) {
        viewModel.getCameraAppData(pkgName)
    }
}


