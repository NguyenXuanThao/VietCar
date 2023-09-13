package com.example.vietcar.ui.home.fragment

import com.example.vietcar.R
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.AddProductDialog
import com.example.vietcar.base.dialogs.ConfirmDialog
import com.example.vietcar.click.ItemCategoryClick
import com.example.vietcar.click.ItemShoppingCartClick
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.category.ListCategory
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.databinding.FragmentHomeBinding
import com.example.vietcar.ui.home.adapter.CategoryAdapter
import com.example.vietcar.ui.home.adapter.ListProductAdapter
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

    private lateinit var frameLayout: FrameLayout

    override fun obServerLivedata() {

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        homeViewModel.categoryResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    categories = resource.data
                    categoryAdapter.differ.submitList(resource.data?.data)
                    binding.rvCategory.adapter = categoryAdapter
                    binding.rvCategory.layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                    frameLayout.visibility = View.GONE
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                    frameLayout.visibility = View.GONE
                }

                is Resource.Loading -> {
                    frameLayout.visibility = View.VISIBLE
                }
            }
        }

        homeViewModel.listProductGroupResponse.observe(viewLifecycleOwner) { resource ->

            when (resource) {
                is Resource.Success -> {
                    listProductAdapter.differ.submitList(resource.data?.data)
                    binding.rvListProduct.adapter = listProductAdapter
                    binding.rvListProduct.layoutManager = LinearLayoutManager(requireContext())
                    frameLayout.visibility = View.GONE
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {
                    frameLayout.visibility = View.VISIBLE
                }
            }

        }

        homeViewModel.productToCartResponse.observe(viewLifecycleOwner) { resource ->

            when (resource) {
                is Resource.Success -> {
                    if (isClickAddProduct) {
                        Toast.makeText(requireContext(), resource.data?.message, Toast.LENGTH_SHORT)
                            .show()
                        isClickAddProduct = false
                    }
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {
                    frameLayout.visibility = View.VISIBLE
                }
            }
        }

        homeViewModel.accountInformationResponse.observe(viewLifecycleOwner) { resource ->

            when (resource) {
                is Resource.Success -> {
                    binding.tvName.text = resource.data?.data?.name
                    binding.tvPhoneNumber.text = resource.data?.data?.phone

                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {
                    frameLayout.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun initData() {
        super.initData()

        retrieveData()

        homeViewModel.getAccountInformation()
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

        binding.imgNotify.setOnClickListener {
            if (status == 0) {

                Log.d("HomeFragment", "tran sit to Notify screen")
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
     * check login
     */

    private fun retrieveData() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val retrievedStatus = sharedPreferences.getInt("status_key", 1)

        status = retrievedStatus

        if (retrievedStatus == 0) {

            updateView()
        }
    }

    private fun updateView() {
        binding.btnHomeLogin.visibility = View.GONE
        binding.imgAvatar.setImageResource(R.drawable.ic_avatar)
        binding.btnVerify.visibility = View.VISIBLE
        binding.tvPhoneNumber.visibility = View.VISIBLE
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
        addProductDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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