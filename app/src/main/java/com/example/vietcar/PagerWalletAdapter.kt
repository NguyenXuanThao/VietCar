package com.example.vietcar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vietcar.ui.PointWalletFragment
import com.example.vietcar.ui.RoseWalletFragment

class PagerWalletAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PointWalletFragment()
            1 -> RoseWalletFragment()
            else -> PointWalletFragment()
        }
    }
}