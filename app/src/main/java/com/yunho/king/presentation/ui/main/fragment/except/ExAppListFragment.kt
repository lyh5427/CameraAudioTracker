package com.yunho.king.presentation.ui.main.fragment.except

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yunho.king.Const
import com.yunho.king.R
import com.yunho.king.Utils.Util
import com.yunho.king.databinding.FragmentExAppListBinding
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.domain.dto.ExAppList
import com.yunho.king.presentation.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExAppListFragment : Fragment() {

    lateinit var binding: FragmentExAppListBinding
    val model: MainViewModel by activityViewModels()

    lateinit var type: String
    var audio: ArrayList<ExAppList> = ArrayList()
    var camera: ArrayList<ExAppList> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExAppListBinding.inflate(inflater, container, false)

        setType()
        lifecycleScope.launch { setObserver() }

        return binding.root
    }

    private fun setType() {
        type = arguments?.getString(Const.TYPE)?: Const.TYPE_CAMERA

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                when (type) {
                    Const.TYPE_CAMERA -> model.getExceptionCameraApp()
                    Const.TYPE_AUDIO -> model.getExceptionAudioApp()
                    else -> {}
                }
            }
        }
    }

    private suspend fun setObserver() {
        when (type) {
            Const.TYPE_CAMERA -> {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    model.cameraList.collect {
                        when (it) {
                            null -> {

                            }

                            else -> makeCameraAppList(it)
                        }
                    }
                }
            }

            Const.TYPE_AUDIO -> {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    model.audioList.collect {
                        when (it) {
                            null -> {

                            }

                            else -> makeAudioAppList(it)
                        }
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
                camera.add(
                    ExAppList(
                    appName = it.appName,
                    appIcon = Util.getAppIcon(
                        it.appPackageName,
                        requireContext())?: requireContext().getDrawable(R.drawable.ic_launcher_background)!!,
                    appPackageName = it.appPackageName,
                    permUseCount = it.permUseCount,
                    lastUseDateTime = it.lastUseDateTime.toLong(),
                    exceptionDate = it.exceptionDate
                )
                )
            }
        }
        setRecyclerView()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun makeAudioAppList(adList: List<AudioAppData>) {
        audio.clear()
        adList.forEach {
            if (it.notiFlag) {
                audio.add(
                    ExAppList(
                    appName = it.appName,
                    appIcon = Util.getAppIcon(
                        it.appPackageName,
                        requireContext())?: requireContext().getDrawable(R.drawable.ic_launcher_background)!!,
                    appPackageName = it.appPackageName,
                    permUseCount = it.permUseCount,
                    lastUseDateTime = it.lastUseDateTime.toLong(),
                    exceptionDate = it.exceptionDate
                )
                )

            }
        }
        setRecyclerView()
    }

    private fun setRecyclerView() = with(binding) {
        appList.adapter = ExAdapter(
            if (type == Const.TYPE_CAMERA) camera else audio,
            requireContext(),
            object: ExAdapterListener {
                override fun deletePackage(pkgName: String) {
                    TODO("Not yet implemented")
                }
            })
    }



    companion object {
        @JvmStatic
        fun newInstance(fragmentType: String) =
            ExAppListFragment().apply {
                arguments = Bundle().apply {
                    putString(Const.TYPE, fragmentType)
                }
            }
    }
}