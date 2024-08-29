package com.yunho.king.presentation.ui.main.fragment.usage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.yunho.king.Const
import com.yunho.king.GlobalApplication
import com.yunho.king.R
import com.yunho.king.Status
import com.yunho.king.Utils.Util
import com.yunho.king.databinding.FragmentAppListBinding
import com.yunho.king.domain.dto.AppList
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.presentation.ui.appdetail.AppDetailActivity
import com.yunho.king.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AppListFragment : Fragment() {

    lateinit var binding: FragmentAppListBinding
    val viewModel: MainViewModel by activityViewModels()

    lateinit var type: String
    var audio: ArrayList<AppList> = ArrayList()
    var camera: ArrayList<AppList> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppListBinding.inflate(inflater)
        type = arguments?.getString(Const.TYPE)?: Const.TYPE_CAMERA

        lifecycleScope.launch { setObserver() }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setType()
    }

    private fun setType() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                when (type) {
                    Const.TYPE_CAMERA -> viewModel.getCameraData()
                    Const.TYPE_AUDIO -> viewModel.getAudioData()
                    else -> {}
                }
            }
        }
    }

    private suspend fun setObserver() {
        when (type) {
            Const.TYPE_CAMERA -> {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.cameraList.collect {
                        makeCameraAppList(it)
                    }
                }
            }

            Const.TYPE_AUDIO -> {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.audioList.collect {
                        makeAudioAppList(it)
                    }
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun makeCameraAppList(caList: List<CameraAppData>) {
        camera.clear()
        caList.forEach {
            if (it.notiFlag) {
                camera.add(AppList(
                    appName = it.appName,
                    appIcon = Util.getAppIcon(
                        it.appPackageName,
                        requireContext())?: requireContext().getDrawable(R.drawable.ic_launcher_background)!!,
                    appPackageName = it.appPackageName,
                    permUseCount = it.permUseCount,
                    lastUseDateTime = it.lastUseDateTime.toLong()
                ))
            }
        }
        setRecyclerView()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun makeAudioAppList(adList: List<AudioAppData>) {
        audio.clear()
        adList.forEach {
            if (it.notiFlag) {
                audio.add(AppList(
                    appName = it.appName,
                    appIcon = Util.getAppIcon(
                        it.appPackageName,
                        requireContext())?: requireContext().getDrawable(R.drawable.ic_launcher_background)!!,
                    appPackageName = it.appPackageName,
                    permUseCount = it.permUseCount,
                    lastUseDateTime = it.lastUseDateTime.toLong()
                ))

            }
        }
        setRecyclerView()
    }

    private fun setRecyclerView() = with(binding) {
        appList.adapter = UsageAdapter(
            if (type == Const.TYPE_CAMERA) camera else audio,
            requireContext(),
            object: UsageAdapterListener{
                override fun moveToDetail(pkgName: String) {
                    startActivity(Intent(requireContext(), AppDetailActivity::class.java).apply {
                        putExtra(Const.PKG_NAME, pkgName)
                    })
                }
            })
    }


    companion object {
        @JvmStatic
        fun newInstance(
            fragmentType: String
        ) = AppListFragment().apply {
            arguments = Bundle().apply {
                putString(Const.TYPE, fragmentType)
            }
        }
    }

}