package com.yunho.king.presentation.ui.perm

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.yunho.king.Utils.PermManager
import com.yunho.king.Utils.singleClickListener
import com.yunho.king.databinding.ActivityPermBinding
import com.yunho.king.presentation.ui.base.BaseActivity
import com.yunho.king.presentation.ui.intro.IntroActivity

class PermActivity : BaseActivity() {

    lateinit var binding: ActivityPermBinding
    lateinit var firstPermissionLauncher: ActivityResultLauncher<String>
    lateinit var secondPermissionLauncher: ActivityResultLauncher<String>
    lateinit var permissionSettingLauncher: ActivityResultLauncher<Intent>
    lateinit var overlayLauncher: ActivityResultLauncher<Intent>
    lateinit var usagesPermLauncher: ActivityResultLauncher<Intent>

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

    fun registOnclickListener() = with(binding) {
        startPerm.singleClickListener {
            startPermAllow()
        }
    }

    fun setLauncher() {
        firstPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (permIndex >= permManager.permSize()) {
                    if (permManager.isRuntimePermAllow()) {
                        moveToOverlayPerm()
                    } else {
                        permIndex = 0
                        secondPermissionLauncher.launch(permManager.isNowPerm(permIndex))
                    }
                } else {
                    permIndex += 1
                    firstPermissionLauncher.launch(permManager.isNowPerm(permIndex))
                }
            }

        secondPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (permIndex >= permManager.permSize()) {
                    if (permManager.isRuntimePermAllow()) {
                        moveToOverlayPerm()
                    } else {
                        moveToApplicationDetail()
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
                    moveToApplicationDetail()
                }
            }

        overlayLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (permManager.isOverlayAllow()) {
                    moveToUsagesPerm()
                } else {
                    moveToOverlayPerm()
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
                    moveToUsagesPerm()
                }
            }
    }

    fun startPermAllow() {
        firstPermissionLauncher.launch(permManager.isNowPerm(permIndex))
    }

    fun moveToApplicationDetail() {
        permissionSettingLauncher.launch(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:${packageName}"))
        )
    }

    fun moveToOverlayPerm() {
        overlayLauncher.launch(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${packageName}")
            )
        )
    }

    fun moveToUsagesPerm() {
        usagesPermLauncher.launch(
            Intent(
                Settings.ACTION_USAGE_ACCESS_SETTINGS,
                Uri.parse("package:${packageName}"))
        )
    }
}