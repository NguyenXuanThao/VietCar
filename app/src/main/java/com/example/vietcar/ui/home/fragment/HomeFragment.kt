package com.example.vietcar.ui.home.fragment

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.ui.home.adapter.CategoryAdapter
import com.example.vietcar.ui.home.adapter.ListProductAdapter
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.AddProductDialog
import com.example.vietcar.base.dialogs.ConfirmDialog
import com.example.vietcar.click.ItemCategoryClick
import com.example.vietcar.click.ItemShoppingCartClick
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.category.ListCategory
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.databinding.FragmentHomeBinding
import com.example.vietcar.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
), ItemCategoryClick, ConfirmDialog.ConfirmCallback, AddProductDialog.AddProductCallBack,
    ItemShoppingCartClick {

    private val homeViewModel: HomeViewModel by viewModels()

    private var productId = 0

    private var categoryAdapter = CategoryAdapter(this)
    private var listProductAdapter = ListProductAdapter(this)

    private var isClickAddProduct = false

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

            updateInfo(loginData!!.data!!.phone.toString())

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

        homeViewModel.productToCartResponse.observe(viewLifecycleOwner) { productToCart ->
            if (isClickAddProduct) {
                Toast.makeText(requireContext(), productToCart.message, Toast.LENGTH_SHORT).show()
                isClickAddProduct = false
            }
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

                transitToShoppingCartScreen()
            } else {
                Utils.showDialogConfirm(requireContext(), this)
            }
        }
    }

    /**
     * translation screen
     */

    private fun transitToShoppingCartScreen() {
        val action = HomeFragmentDirections.actionBottomNavHomeToShoppingCartFragment()
        findNavController().navigate(action)
    }

    private fun transitToLoginScreen() {
        val action = HomeFragmentDirections.actionBottomNavHomeToLoginFragment()
        findNavController().navigate(action)
    }

    /**
     * save data
     */
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

            updateInfo(retrievedPhone!!)
        }
    }

    private fun updateInfo(retrievedPhone: String) {
        binding.btnHomeLogin.visibility = View.GONE
        binding.btnVerify.visibility = View.VISIBLE
        binding.tvPhoneNumber.visibility = View.VISIBLE
        binding.tvPhoneNumber.text = retrievedPhone
        binding.imgWarning.visibility = View.VISIBLE
    }

    /**
     * onItemClick listener
     */

    override fun onClickCategoryItem(position: Int) {
        val action =
            HomeFragmentDirections.actionBottomNavHomeToCategoryFragment(position, categories)
        findNavController().navigate(action)
    }

    override fun onClickShoppingCartItem(product: Product) {

        if (status == 0) {
            showDialogProductInfo(product)
        } else {
            Utils.showDialogConfirm(requireContext(), this)
        }
    }

    /**
     * Confirm dialog
     */
    override fun confirmTranSitToLoginScreen() {
        transitToLoginScreen()
    }

    /**
     * Add Product Dialog
     */

    private fun showDialogProductInfo(product: Product) {
        productId = product.id!!
        val addProductDialog = AddProductDialog(
            requireContext(),
            this,
            product.name,
            product.net_price.toString(),
            product.avatar,
            "Thêm vào giỏ"
        )
        addProductDialog.show()
        addProductDialog.window?.setGravity(Gravity.CENTER)
        addProductDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun clickAddProduct(number: Int) {
        isClickAddProduct = true
        Log.d("HomeFragment", "number $number; product id $productId")

        val body = ProductBody(productId, number)
        homeViewModel.addProductToCart(body)
    }
}