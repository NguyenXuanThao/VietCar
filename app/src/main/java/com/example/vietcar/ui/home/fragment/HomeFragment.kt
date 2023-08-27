package com.example.vietcar.ui.home.fragment

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.ui.home.adapter.CategoryAdapter
import com.example.vietcar.ui.home.adapter.ListProductAdapter
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.ConfirmDialog
import com.example.vietcar.base.dialogs.ErrorDialog
import com.example.vietcar.click.ItemCategoryClick
import com.example.vietcar.data.model.category.Category
import com.example.vietcar.data.model.category.ListCategory
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.databinding.FragmentHomeBinding
import com.example.vietcar.ui.customer.login.LoginViewModel
import com.example.vietcar.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
), ItemCategoryClick, ConfirmDialog.ConfirmCallback {

    private val homeViewModel: HomeViewModel by viewModels()

    private var categoryAdapter = CategoryAdapter(this)
    private var listProductAdapter = ListProductAdapter()

    private var status = 1

    private var categories: ListCategory? = null

    override fun checkLogin() {
        super.checkLogin()
        Log.d("HomeFragment", "checkLogin")

        parentFragmentManager.setFragmentResultListener(
            "loginResult",
            viewLifecycleOwner
        ) { _, result ->
            val loginData = result.getParcelable<LoginResponse>("loginData")

            binding.btnHomeLogin.visibility = View.GONE
            binding.btnVerify.visibility = View.VISIBLE
            binding.tvPhoneNumber.visibility = View.VISIBLE
            binding.tvPhoneNumber.text = loginData!!.data!!.phone.toString()
            binding.imgWarning.visibility = View.VISIBLE

            status = loginData.status!!
            saveData(loginData.status, loginData.data!!.phone.toString())

            Log.d("HomeFragment", loginData.status.toString())
        }

        retrieveData()

    }

    override fun obServerLivedata() {

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
            if (status == 0) {

                val action = HomeFragmentDirections.actionBottomNavHomeToShoppingCartFragment()
                findNavController().navigate(action)
            }else {
                showDialogConfirm()
            }
        }
    }

    private fun showDialogConfirm() {

        val confirmDialog = ConfirmDialog(requireContext(), this, "Bạn chưa đăng nhập. Đăng nhập ngay bây gi để thực hiện chức năng này?", "Đồng ý", "Hủy")
        confirmDialog.show()
        confirmDialog.window?.setGravity(Gravity.CENTER)
        confirmDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun transitToLoginScreen() {
        val action = HomeFragmentDirections.actionBottomNavHomeToLoginFragment()
        findNavController().navigate(action)
    }

    private fun saveData(status: Int, phoneNumber: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("status_key", status)
        editor.putString("number_key", phoneNumber)

        editor.apply()
    }

    private fun retrieveData() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val retrievedPhone = sharedPreferences.getString("number_key", "")
        val retrievedStatus = sharedPreferences.getInt("status_key", 1)

        status = retrievedStatus

        if (retrievedPhone != "" && retrievedStatus != 1) {
            Log.d("ThaoNX", "retrieveData: $retrievedPhone & $retrievedStatus")

            binding.btnHomeLogin.visibility = View.GONE
            binding.btnVerify.visibility = View.VISIBLE
            binding.tvPhoneNumber.visibility = View.VISIBLE
            binding.tvPhoneNumber.text = retrievedPhone.toString()
            binding.imgWarning.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(position: Int) {
        val action =
            HomeFragmentDirections.actionBottomNavHomeToCategoryFragment(position, categories)
        findNavController().navigate(action)
    }

    override fun negativeAction() {
        Log.d("HomeFragment", "Hủy bỏ")
    }

    override fun positiveAction() {
       transitToLoginScreen()
    }
}