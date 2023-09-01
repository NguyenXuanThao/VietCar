package com.example.vietcar

import com.example.vietcar.base.BaseFragment
import com.example.vietcar.databinding.FragmentWalletBinding
import com.google.android.material.tabs.TabLayoutMediator


class WalletFragment : BaseFragment<FragmentWalletBinding>(
    FragmentWalletBinding::inflate
) {

    val tabs = arrayListOf("Ví điểm", "Ví hoa hồng")

    override fun initView() {
        super.initView()

        setupTabLayout()
    }

    private fun setupTabLayout() {
        val pagerWalletAdapter = PagerWalletAdapter(requireActivity())
        binding.vpWallet.adapter = pagerWalletAdapter
        binding.vpWallet.currentItem = 0
        binding.vpWallet.isUserInputEnabled = false
        TabLayoutMediator(binding.tabWallet, binding.vpWallet) { tab, index ->
            tab.text = tabs[index]
        }.attach()
    }

}