package com.example.vietcar.ui.home.fragment

import com.example.vietcar.R
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
import com.bumptech.glide.Glide
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.AddProductDialog
import com.example.vietcar.base.dialogs.ConfirmDialog
import com.example.vietcar.click.ItemCategoryClick
import com.example.vietcar.click.ItemShoppingCartClick
import com.example.vietcar.common.DataLocal
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

    private var categories: ListCategory? = null

    private lateinit var frameLayout: FrameLayout

    private fun setVisibility(isLoading: Boolean) {
        frameLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun obServerLivedata() {

        frameLayout = requireActivity().findViewById(R.id.frameLayout)

        homeViewModel.bannerResponse.observe(viewLifecycleOwner) { resource ->

            setVisibility(resource is Resource.Loading)

            when (resource) {
                is Resource.Success -> {
                    val banners = resource.data

                    val uriImage = "https://vietcargroup.com${banners?.data!![0].url_image}"
                    Glide.with(requireContext()).load(uriImage)
                        .into(binding.imgPromote)
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {

                }
            }
        }

        homeViewModel.categoryResponse.observe(viewLifecycleOwner) { resource ->

            setVisibility(resource is Resource.Loading)

            when (resource) {
                is Resource.Success -> {
                    categories = resource.data
                    categoryAdapter.differ.submitList(resource.data?.data)
                    binding.rvCategory.adapter = categoryAdapter
                    binding.rvCategory.layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {
                }
            }
        }

        homeViewModel.listProductGroupResponse.observe(viewLifecycleOwner) { resource ->

            setVisibility(resource is Resource.Loading)

            when (resource) {
                is Resource.Success -> {
                    listProductAdapter.differ.submitList(resource.data?.data)
                    binding.rvListProduct.adapter = listProductAdapter
                    binding.rvListProduct.layoutManager = LinearLayoutManager(requireContext())
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {
                }
            }

        }

        homeViewModel.productToCartResponse.observe(viewLifecycleOwner) { resource ->

            setVisibility(resource is Resource.Loading)

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
                }
            }
        }

        homeViewModel.accountInformationResponse.observe(viewLifecycleOwner) { resource ->

            setVisibility(resource is Resource.Loading)

            when (resource) {
                is Resource.Success -> {
                    val accountInformation = resource.data?.data
                    binding.tvName.text = accountInformation?.name
                    binding.tvPhoneNumber.text = accountInformation?.phone

                    val uriImage = "https://vietcargroup.com${accountInformation?.image}"
                    Glide.with(requireContext()).load(uriImage)
                        .into(binding.imgAvatar)

                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {
                }
            }

        }
    }

    override fun initData() {
        super.initData()

        homeViewModel.getBanner()
        homeViewModel.getCategory()
        homeViewModel.getProductGroup()

        Log.d("BaseFragment", DataLocal.STATUS.toString())

        if (DataLocal.STATUS == 0) {

            homeViewModel.getAccountInformation()

            updateView()
        }

    }

    override fun evenClick() {
        super.evenClick()

        binding.imgSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionBottomNavHomeToSearchFragment()
            findNavController().navigate(action)
        }

        binding.btnHomeLogin.setOnClickListener {
            transitToLoginScreen()
        }

        binding.imgShopping.setOnClickListener {
            if (DataLocal.STATUS == 0) {

                transitToShoppingCartScreen()
            } else {
                Utils.showDialogConfirm(
                    requireContext(),
                    "Bạn chưa đăng nhập. Đăng nhập ngay bây gi để thực hiện chức năng này?",
                    this
                )
            }
        }

        binding.imgNotify.setOnClickListener {
            if (DataLocal.STATUS == 0) {

                Log.d("HomeFragment", "tran sit to Notify screen")
            } else {
                Utils.showDialogConfirm(
                    requireContext(),
                    "Bạn chưa đăng nhập. Đăng nhập ngay bây gi để thực hiện chức năng này?",
                    this
                )
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

    private fun updateView() {
        binding.imgAvatar.visibility = View.VISIBLE
        binding.imgAvatarFake.visibility = View.GONE
        binding.btnHomeLogin.visibility = View.GONE
        binding.tvHello.visibility = View.VISIBLE
        binding.tvName.visibility = View.VISIBLE
        binding.tvPhoneNumber.visibility = View.VISIBLE
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

        if (DataLocal.STATUS == 0) {
            showDialogProductInfo(product)
        } else {
            Utils.showDialogConfirm(
                requireContext(),
                "Bạn chưa đăng nhập. Đăng nhập ngay bây gi để thực hiện chức năng này?",
                this
            )
        }
    }

    /**
     * Confirm dialog
     */
    override fun onClickConfirm() {
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
        addProductDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
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