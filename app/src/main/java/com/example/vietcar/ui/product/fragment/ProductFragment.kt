package com.example.vietcar.ui.product.fragment

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.AddProductDialog
import com.example.vietcar.base.dialogs.ConfirmDialog
import com.example.vietcar.click.ItemShoppingCartClick
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.databinding.FragmentProductBinding
import com.example.vietcar.ui.product.adapter.ProductAdapter
import com.example.vietcar.ui.product.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : BaseFragment<FragmentProductBinding>(
    FragmentProductBinding::inflate
), ItemShoppingCartClick, ConfirmDialog.ConfirmCallback, AddProductDialog.AddProductCallBack {

    private val productViewModel: ProductViewModel by viewModels()
    private var productAdapter = ProductAdapter(this)

    private var status = 1

    private var productId = 0

    private var isClickAddProduct = false

    override fun checkLogin() {
        super.checkLogin()

        Log.d("ProductFragment", "checkLogin")

        parentFragmentManager.setFragmentResultListener(
            "loginResult", viewLifecycleOwner
        ) { _, result ->
            val loginData = result.getParcelable<LoginResponse>("loginData")

            status = loginData!!.status!!
            saveData(loginData.status!!)

            Log.d("ProductFragment", loginData.status.toString())
        }

        retrieveData()
    }

    override fun obServerLivedata() {
        productViewModel.productResponse.observe(viewLifecycleOwner) { products ->
            productAdapter.differ.submitList(products.data)
            binding.rvProductFragment.adapter = productAdapter
            binding.rvProductFragment.layoutManager = GridLayoutManager(requireContext(), 2)
        }

        productViewModel.productToCartResponse.observe(viewLifecycleOwner) { productToCart ->
            if (isClickAddProduct) {
                Toast.makeText(requireContext(), productToCart.message, Toast.LENGTH_SHORT).show()
                isClickAddProduct = false
            }
        }
    }

    override fun initData() {
        productViewModel.getAllProduct()
    }

    /**
     * translation screen
     */
    private fun transitToLoginScreen() {
        val action = ProductFragmentDirections.actionBottomNavProductToLoginFragment()
        findNavController().navigate(action)
    }

    /**
     * save data
     */
    private fun saveData(status: Int) {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("status_key", status)

        editor.apply()
    }

    private fun retrieveData() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val retrievedStatus = sharedPreferences.getInt("status_key", 1)

        status = retrievedStatus
    }

    /**
     * onItemClick listener
     */
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
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun clickAddProduct(number: Int) {
        isClickAddProduct = true
        Log.d("CategoryFragment", "number $number; product id $productId")

        val body = ProductBody(productId, number)
        productViewModel.addProductToCart(body)
    }

}