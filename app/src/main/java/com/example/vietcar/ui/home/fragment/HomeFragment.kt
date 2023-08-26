package com.example.vietcar.ui.home.fragment

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.ui.home.adapter.CategoryAdapter
import com.example.vietcar.ui.home.adapter.ListProductAdapter
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.click.ItemCategoryClick
import com.example.vietcar.data.model.category.Category
import com.example.vietcar.data.model.category.ListCategory
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.databinding.FragmentHomeBinding
import com.example.vietcar.ui.customer.login.LoginViewModel
import com.example.vietcar.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
), ItemCategoryClick {

    private val homeViewModel: HomeViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    private var categoryAdapter = CategoryAdapter(this)
    private var listProductAdapter = ListProductAdapter()

    private var phoneNumber: String? = null
    private var password: String? = null
    private var categories: ListCategory? = null

    override fun checkLogin() {
        super.checkLogin()

        parentFragmentManager.setFragmentResultListener(
            "loginResult",
            viewLifecycleOwner
        ) { _, result ->

            phoneNumber = result.getString("phoneNumber")
            password = result.getString("password")

            if (phoneNumber != null && password != null) {
                saveData(phoneNumber!!, password!!)
            } else {
                Log.d("ThaoNX", "data is null")
            }
            retrieveData()
        }

        retrieveData()
    }

    override fun obServerLivedata() {

        loginViewModel.loginResponse.observe(this) { loginResponse ->

            if (loginResponse != null) {
                loginResponse.message?.let { Log.d("HomeFragment", it) }

                binding.btnHomeLogin.visibility = View.GONE
                binding.btnVerify.visibility = View.VISIBLE
                binding.tvPhoneNumber.visibility = View.VISIBLE
                binding.tvPhoneNumber.text = loginResponse.data!!.phone.toString()
                binding.imgWarning.visibility = View.VISIBLE
            } else {
                Log.d("HomeFragment", "null")
            }
        }

        homeViewModel.categoryResponse.observe(viewLifecycleOwner) { listCategory ->

            categories = listCategory

            categoryAdapter.differ.submitList(listCategory.data)
            binding.rvCategory.adapter = categoryAdapter
            binding.rvCategory.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        homeViewModel.listProductGroupResponse.observe(viewLifecycleOwner) { products ->

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
            transitToLoginScreen()
        }

        binding.imgShopping.setOnClickListener {
//            Toast.makeText(requireContext(), "đề ngày mai đi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun transitToLoginScreen() {
        val action = HomeFragmentDirections.actionBottomNavHomeToLoginFragment()
        findNavController().navigate(action)
    }

    private fun saveData(phoneNumber: String, password: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("number_key", phoneNumber)
        editor.putString("password_key", password)

        editor.apply()
    }

    private fun retrieveData() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val retrievedNumber = sharedPreferences.getString("number_key", "")
        val retrievedPassword = sharedPreferences.getString("password_key", "")

        if (retrievedNumber != "" && retrievedPassword != "") {
            Log.d("ThaoNX", "retrieveData: $retrievedNumber & $retrievedPassword")

            obServerLivedata()
            val bodyLogin = LoginBody(phone = retrievedNumber!!, password = retrievedPassword!!)
            loginViewModel.login(bodyLogin)
        }
    }

    override fun onItemClick(position: Int) {
        val action = HomeFragmentDirections.actionBottomNavHomeToCategoryFragment(position, categories)
       findNavController().navigate(action)
    }
}