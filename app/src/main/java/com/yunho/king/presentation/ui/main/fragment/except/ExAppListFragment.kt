package com.yunho.king.presentation.ui.main.fragment.except

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yunho.king.Const
import com.yunho.king.GlobalApplication
import com.yunho.king.R
import com.yunho.king.Utils.Util
import com.yunho.king.Utils.toGone
import com.yunho.king.Utils.toVisible
import com.yunho.king.databinding.FragmentExAppListBinding
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.domain.dto.ExAppList
import com.yunho.king.presentation.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
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
                    Const.TYPE_CAMERA -> model.getExceptionCameraApp()
                    Const.TYPE_AUDIO -> model.getExceptionAudioApp()
                    else -> {}
                }
            }
        }
    }

    private suspend fun setObserver() = with(binding) {
        when (type) {
            Const.TYPE_CAMERA -> {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    model.exCameraList.collect {
                        when (it) {
                            null -> {
                                txtNonList.toVisible()
                                appList.toGone()
                            }

                            else -> makeCameraAppList(it)
                        }
                    }
                }
            }

            Const.TYPE_AUDIO -> {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    model.exAudioList.collect {
                        when (it) {
                            null -> {
                                txtNonList.toVisible()
                                appList.toGone()
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
        Log.d(GlobalApplication.TagName, "aaaaaaaaaaaa")

        camera.clear()
        caList.forEach {
            camera.add(
                ExAppList(
                    appName = it.appName,
                    appIcon = Util.getAppIcon(
                        it.appPackageName,
                        requireContext())?: requireContext().getDrawable(R.drawable.ic_launcher_background)!!,
                    appPackageName = it.appPackageName,
                    permUseCount = it.permUseCount,
                    lastUseDateTime = it.lastUseDateTime,
                    exceptionDate = it.exceptionDate
                )
            )
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
                    lastUseDateTime = it.lastUseDateTime,
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
                    CoroutineScope(Dispatchers.IO).launch {
                        model.updateCameraAppFlag(pkgName, true)
                    }
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