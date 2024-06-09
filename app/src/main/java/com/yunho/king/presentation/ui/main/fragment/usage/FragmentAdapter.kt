package com.yunho.king.presentation.ui.main.fragment.usage
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class FragmentAdapter(fragmentActivity: Fragment) :
    FragmentStateAdapter(fragmentActivity) {
    // 프래그먼트들의 배열을 만들어서 관리
    var fragmentsList = ArrayList<Fragment>()

    override fun getItemCount(): Int {
        return fragmentsList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position]
    }

    fun addFragments(fragment: Fragment) {
        fragmentsList.add(fragment)
        notifyItemInserted(fragmentsList.size - 1)
    }
}