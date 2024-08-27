package com.yunho.king.presentation.ui.main.fragment.except

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.yunho.king.Const
import com.yunho.king.R
import com.yunho.king.Utils.Util
import com.yunho.king.databinding.FragmentExceptBinding
import com.yunho.king.domain.dto.AppList
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.domain.dto.ExAppList
import com.yunho.king.presentation.ui.appdetail.AppDetailActivity
import com.yunho.king.presentation.ui.main.MainViewModel
import com.yunho.king.presentation.ui.main.fragment.usage.AppListFragment
import com.yunho.king.presentation.ui.main.fragment.usage.FragmentAdapter
import com.yunho.king.presentation.ui.main.fragment.usage.UsageAdapter
import com.yunho.king.presentation.ui.main.fragment.usage.UsageAdapterListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExceptFragment : Fragment() {

    lateinit var binding: FragmentExceptBinding
    val model: MainViewModel by activityViewModels()

    private lateinit var fragCamera: ExAppListFragment
    private lateinit var fragAudio: ExAppListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExceptBinding.inflate(inflater, container, false)

        fragCamera = ExAppListFragment.newInstance(Const.TYPE_CAMERA)
        fragAudio = ExAppListFragment.newInstance(Const.TYPE_AUDIO)

        setTabAdapter()

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun setTabAdapter() = with(binding) {
        val adapter = FragmentAdapter(this@ExceptFragment).apply {
            addFragments(fragCamera)
            addFragments(fragAudio)
        }
        usageFragViewPager.adapter = adapter

        TabLayoutMediator(layoutTab, usageFragViewPager) {tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.camera_tab)
                else -> getString(R.string.audio_tab)
            }
        }.attach()

        layoutTab.selectTab(layoutTab.getTabAt(0))
    }


    companion object {
        @JvmStatic
        fun newInstance( ) =
            ExceptFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}