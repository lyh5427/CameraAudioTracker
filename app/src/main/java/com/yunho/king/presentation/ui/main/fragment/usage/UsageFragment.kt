package com.yunho.king.presentation.ui.main.fragment.usage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.yunho.king.R
import com.yunho.king.databinding.FragmentUsageBinding
import com.yunho.king.presentation.ui.main.MainViewModel

class UsageFragment : Fragment() {

    lateinit var binding: FragmentUsageBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = FragmentUsageBinding.inflate(inflater, container, false)


        return binding.root
    }

}