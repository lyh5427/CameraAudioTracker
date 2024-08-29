package com.yunho.king.presentation.ui.perm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.yunho.king.Const
import com.yunho.king.Const.TAG_OVERLAY_DENIED
import com.yunho.king.Const.TAG_PERM_DENIED
import com.yunho.king.Const.TAG_USAGE_DENIED_DIALOG
import com.yunho.king.Const.TAG_USAGE_DIALOG
import com.yunho.king.R
import com.yunho.king.Utils.PermManager
import com.yunho.king.Utils.singleClickListener
import com.yunho.king.databinding.ActivityPermBinding
import com.yunho.king.domain.listener.DialogNormalType1Listener
import com.yunho.king.domain.listener.DialogNormalType2Listener
import com.yunho.king.presentation.ui.base.BaseActivity
import com.yunho.king.presentation.ui.dialog.DialogTypeNormal
import com.yunho.king.presentation.ui.intro.IntroActivity
import com.yunho.king.presentation.ui.main.MainActivity

class PermActivity : BaseActivity(), DialogNormalType1Listener, DialogNormalType2Listener {

    private lateinit var binding: ActivityPermBinding
    private lateinit var firstPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var secondPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var permissionSettingLauncher: ActivityResultLauncher<Intent>
    private lateinit var overlayLauncher: ActivityResultLauncher<Intent>
    private lateinit var usagesPermLauncher: ActivityResultLauncher<Intent>
    private lateinit var dialogNormal: DialogTypeNormal

    lateinit var permManager: PermManager

    var permIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermBinding.inflate(layoutInflater)

        permManager = PermManager(this)

        setLauncher()
        registOnclickListener()
        setContentView(binding.root)
    }

    private fun registOnclickListener() = with(binding) {
        startPerm.singleClickListener {
            startPermAllow()
        }
    }

    private fun setLauncher() {
        firstPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (permIndex >= permManager.permSize()) {
                    permIndex = 0
                    secondPermissionLauncher.launch(permManager.isNowPerm(permIndex))
                } else {
                    permIndex += 1
                    firstPermissionLauncher.launch(permManager.isNowPerm(permIndex))
                }
            }

        secondPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (permIndex >= permManager.permSize()) {
                    if (permManager.isRuntimePermAllow()) {
                        if (permManager.isOverlayAllow()) {
                            showReqUsagePermDialog()
                        } else {
                            moveToOverlayPerm()
                        }
                    } else {
                        showPermDeniedDialog()
                    }
                } else {
                    permIndex += 1
                    secondPermissionLauncher.launch(permManager.isNowPerm(permIndex))
                }
            }

        permissionSettingLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (permManager.isRuntimePermAllow()) {
                    moveToOverlayPerm()
                } else {
                    showPermDeniedDialog()
                }
            }

        overlayLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (permManager.isOverlayAllow()) {
                    showReqUsagePermDialog()
                } else {
                    showOverlayDeniedDialog()
                }

            }

        usagesPermLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (permManager.isUsagesPermAllow()) {
                    moveToIntent(
                        Intent(this, IntroActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        }
                    )
                } else {
                    showUsagePermDeniedDialog()
                }
            }
    }

    override fun onClick() {
        when (dialogNormal.tag) {
            TAG_PERM_DENIED -> moveToApplicationDetail()
            TAG_OVERLAY_DENIED -> moveToOverlayPerm()
            TAG_USAGE_DENIED_DIALOG -> {
                moveToIntent(Intent(this, IntroActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
            }
            else -> {}
        }
    }

    override fun clickOk() {
        when (dialogNormal.tag) {
            TAG_USAGE_DIALOG -> moveToUsagesPerm()
        }
    }

    override fun clickCancel() {
        showUsagePermDeniedDialog()
    }

    private fun startPermAllow() {
        firstPermissionLauncher.launch(permManager.isNowPerm(permIndex))
    }

    private fun showPermDeniedDialog() {
        dialogNormal = DialogTypeNormal.newInstance(
            title = getString(R.string.perm_denied_title),
            content = getString(R.string.perm_denied_content),
            type = Const.DIALOG_TYPE1,
            okText = getString(R.string.setting),
            cancelText = getString(R.string.cancel),
            cancelable = true,
        )

        dialogNormal.regType1Listener(this)

        supportFragmentManager.beginTransaction().add(dialogNormal, TAG_PERM_DENIED)
            .commitAllowingStateLoss()
    }

    private fun showOverlayDeniedDialog() {
        dialogNormal = DialogTypeNormal.newInstance(
            title = getString(R.string.perm_denied_overlay_title),
            content = getString(R.string.perm_denied_overlay_content),
            type = Const.DIALOG_TYPE1,
            okText = getString(R.string.setting),
            cancelText = "",
            cancelable = true
        )

        dialogNormal.regType1Listener(this)

        supportFragmentManager.beginTransaction().add(dialogNormal, TAG_OVERLAY_DENIED)
            .commitAllowingStateLoss()
    }

    private fun showReqUsagePermDialog() {
        dialogNormal = DialogTypeNormal.newInstance(
            title = getString(R.string.perm_usage_title),
            content = getString(R.string.perm_usage_content),
            type = Const.DIALOG_TYPE2,
            okText = getString(R.string.setting),
            cancelText = getString(R.string.cancel),
            cancelable = true
        )

        dialogNormal.regType2Listener(this)

        supportFragmentManager.beginTransaction().add(dialogNormal, TAG_USAGE_DIALOG)
            .commitAllowingStateLoss()
    }

    private fun showUsagePermDeniedDialog() {
        dialogNormal = DialogTypeNormal.newInstance(
            title = getString(R.string.perm_denied_usage_title),
            content =getString(R.string.perm_denied_usage_content),
            type = Const.DIALOG_TYPE1,
            okText = getString(R.string.cancel),
            cancelText = "",
            cancelable = true
        )

        dialogNormal.regType1Listener(object : DialogNormalType1Listener{
            override fun onClick() {
                showReqUsagePermDialog()
            }
        })

        supportFragmentManager.beginTransaction().add(dialogNormal, TAG_USAGE_DENIED_DIALOG)
            .commitAllowingStateLoss()
    }

    private fun moveToApplicationDetail() {
        permissionSettingLauncher.launch(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:${packageName}"))
        )
    }

    private fun moveToOverlayPerm() {
        overlayLauncher.launch(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${packageName}")
            )
        )
    }

    private fun moveToUsagesPerm() {
        usagesPermLauncher.launch(
            Intent(
                Settings.ACTION_USAGE_ACCESS_SETTINGS,
                Uri.parse("package:${packageName}"))
        )
    }
}