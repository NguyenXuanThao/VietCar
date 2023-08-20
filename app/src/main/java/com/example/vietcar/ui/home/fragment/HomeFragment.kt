package com.example.vietcar.ui.home.fragment

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.activities.login.LoginActivity
import com.example.vietcar.ui.home.adapter.CategoryAdapter
import com.example.vietcar.ui.home.adapter.ListProductAdapter
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.databinding.FragmentHomeBinding
import com.example.vietcar.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    private val homeViewModel: HomeViewModel by viewModels()

    private var categoryAdapter = CategoryAdapter()
    private var listProductAdapter = ListProductAdapter()

    override fun obServerLivedata() {
        homeViewModel.categoryResponse.observe(viewLifecycleOwner) { category ->
            categoryAdapter.differ.submitList(category.data)
            binding.rvCategory.adapter = categoryAdapter
            binding.rvCategory.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        homeViewModel.productGroupResponse.observe(viewLifecycleOwner) { products ->

            listProductAdapter.differ.submitList(products.data)
            binding.rvListProduct.adapter = listProductAdapter
            binding.rvListProduct.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun initData() {
        super.initData()
        homeViewModel.getCategory()
        homeViewModel.getProductGroup()
    }

    override fun evenClick() {
        super.evenClick()

        binding.btnHomeLogin.setOnClickListener {
            openLoginActivity()
        }
    }

    private fun openLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }
}