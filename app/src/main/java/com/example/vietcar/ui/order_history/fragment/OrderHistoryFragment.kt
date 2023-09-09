package com.example.vietcar.ui.order_history.fragment

import android.view.View
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.databinding.FragmentOrderHistoryBinding
import com.example.vietcar.ui.order_history.adapter.PagerOrderHistoryAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator


class OrderHistoryFragment : BaseFragment<FragmentOrderHistoryBinding>(
    FragmentOrderHistoryBinding::inflate
) {

    private val tabs = arrayListOf("Chờ xác nhận", "Đã hủy")

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onResume() {
        super.onResume()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE

        setupTabLayout()
    }

    private fun setupTabLayout() {
        val pagerOrderHistoryAdapter = PagerOrderHistoryAdapter(requireActivity())
        binding.vpOrderHistory.adapter = pagerOrderHistoryAdapter
        binding.vpOrderHistory.currentItem = 0
        binding.vpOrderHistory.isUserInputEnabled = false
        TabLayoutMediator(binding.tabOrderHistory, binding.vpOrderHistory) { tab, index ->
            tab.text = tabs[index]
        }.attach()
    }

}