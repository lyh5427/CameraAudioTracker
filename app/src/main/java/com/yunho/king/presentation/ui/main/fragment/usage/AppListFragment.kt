package com.yunho.king.presentation.ui.main.fragment.usage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.yunho.king.Const
import com.yunho.king.R
import com.yunho.king.Utils.Util
import com.yunho.king.databinding.FragmentAppListBinding
import com.yunho.king.domain.dto.AppList
import com.yunho.king.presentation.ui.main.MainViewModel

class AppListFragment : Fragment() {

    lateinit var binding: FragmentAppListBinding
    val viewModel: MainViewModel by activityViewModels()

    lateinit var type: String
    var itemList: ArrayList<AppList> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAppListBinding.inflate(inflater)
        setType()
        setRecyclerView()
        return binding.root
    }

    private fun setType() {
        type = arguments?.getString(Const.TYPE)?: Const.TYPE_CAMERA
    }

    private fun setRecyclerView() = with(binding) {
        when (type) {
            Const.TYPE_CAMERA -> makeCameraAppList()
            else -> makeAudioAppList()
        }

        binding.appList.adapter = UsageAdapter(itemList, requireContext(),
            object: UsageAdapterListener{
            override fun moveToDetail(pkgName: String) {
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:${pkgName}"))) }
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun makeCameraAppList() {
        itemList.clear()
        viewModel.getCameraData().forEach {
            if (it.notiFlag) {
                itemList.add(AppList(
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

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun makeAudioAppList() {
        itemList.clear()
        viewModel.getAudioData().forEach {
            if (it.notiFlag) {
                itemList.add(AppList(
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