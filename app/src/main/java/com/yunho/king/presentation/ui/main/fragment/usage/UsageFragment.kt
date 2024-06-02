package com.yunho.king.presentation.ui.main.fragment.usage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.replace
import com.google.android.material.tabs.TabLayout
import com.yunho.king.Const
import com.yunho.king.R
import com.yunho.king.databinding.FragmentUsageBinding
import com.yunho.king.domain.dto.AppList
import com.yunho.king.presentation.ui.main.MainViewModel

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
        layoutTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setFragment(
                    when (tab!!.id) {
                    R.id.tabCamera -> fragCamera
                    else -> fragAudio
                })
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun setFragment(frag: AppListFragment) {
        childFragmentManager.beginTransaction().replace(
            R.id.usageFragmentContainer, frag
        ).commitAllowingStateLoss()
    }
}