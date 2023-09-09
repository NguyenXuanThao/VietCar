package com.example.vietcar.ui.order_history.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vietcar.ui.cancel_order.fragment.OrderCancellationFragment
import com.example.vietcar.ui.confirm_order.fragment.OrderConfirmFragment

class PagerOrderHistoryAdapter(fragmentActivity: FragmentActivity) :
FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderConfirmFragment()
            1 -> OrderCancellationFragment()
            else -> OrderConfirmFragment()
        }
    }
}