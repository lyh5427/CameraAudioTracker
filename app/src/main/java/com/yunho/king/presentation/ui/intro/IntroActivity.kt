package com.yunho.king.presentation.ui.intro

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.yunho.king.BuildConfig
import com.yunho.king.R
import com.yunho.king.databinding.ActivityIntroBinding
import com.yunho.king.presentation.ui.main.MainActivity

class IntroActivity : AppCompatActivity() {

    lateinit var binding: ActivityIntroBinding

    lateinit var firstPermission: ActivityResultLauncher<String>
    lateinit var secondPermission: ActivityResultLauncher<String>
    lateinit var intentPermission: ActivityResultLauncher<Intent>

    private var permIndex: Int = 0

    private var permissionList: ArrayList<String> = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        arrayListOf(
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.CAMERA
        )
    }
    else {
        arrayListOf(
            android.Manifest.permission.CAMERA
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        binding.lifecycleOwner = this

        firstPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(permissionList.size-1 >= permIndex){
                if(it){
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                }
            }
            else {
                permIndex+=1
                firstPermission.launch(permissionList[permIndex])
            }
        }

        firstPermission.launch(permissionList[0])
    }
}