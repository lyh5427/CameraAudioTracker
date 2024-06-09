package com.yunho.king.presentation.ui.main.fragment.usage

import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.replace
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yunho.king.Const
import com.yunho.king.GlobalApplication
import com.yunho.king.R
import com.yunho.king.databinding.FragmentUsageBinding
import com.yunho.king.domain.dto.AppList
import com.yunho.king.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.newFixedThreadPoolContext

@AndroidEntryPoint
class UsageFragment : Fragment() {

    private lateinit var binding: FragmentUsageBinding
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var fragCamera: AppListFragment
    private lateinit var fragAudio: AppListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsageBinding.inflate(inflater, container, false)


        fragCamera = AppListFragment.newInstance(Const.TYPE_CAMERA)
        fragAudio = AppListFragment.newInstance(Const.TYPE_AUDIO)

        setTabAdapter()

        return binding.root
    }

    private fun setTabAdapter() = with(binding) {
        val adapter = FragmentAdapter(this@UsageFragment).apply {
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

    private fun setFragment(frag: AppListFragment) {
        childFragmentManager.beginTransaction().replace(
            R.id.usageFragViewPager, frag
        ).commitAllowingStateLoss()
    }
}