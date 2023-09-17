package com.example.vietcar.ui.detail_search.fragment


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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.AddProductDialog
import com.example.vietcar.base.dialogs.ConfirmDialog
import com.example.vietcar.click.ItemShoppingCartClick
import com.example.vietcar.common.DataLocal
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.databinding.FragmentDetailSearchBinding
import com.example.vietcar.ui.detail_search.viewmodel.DetailSearchViewModel
import com.example.vietcar.ui.product.adapter.ProductAdapter
import com.example.vietcar.ui.product.fragment.ProductFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailSearchFragment : BaseFragment<FragmentDetailSearchBinding>(
    FragmentDetailSearchBinding::inflate
), ItemShoppingCartClick, ConfirmDialog.ConfirmCallback, AddProductDialog.AddProductCallBack {

    private val detailSearchViewModel: DetailSearchViewModel by viewModels()

    private var productId = 0

    private var isClickAddProduct = false

    private lateinit var frameLayout: FrameLayout

    private var productAdapter = ProductAdapter(this)

    private val args: DetailSearchFragmentArgs by navArgs()

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        detailSearchViewModel.productResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    productAdapter.differ.submitList(resource.data?.data)
                    binding.rvDetailSearch.adapter = productAdapter
                    binding.rvDetailSearch.layoutManager = GridLayoutManager(requireContext(), 2)
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

        detailSearchViewModel.productToCartResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (isClickAddProduct) {
                        Toast.makeText(requireContext(), resource.data?.message, Toast.LENGTH_SHORT)
                            .show()
                        isClickAddProduct = false
                    }
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
    }

    override fun initData() {
        super.initData()

        val keySearch = args.keySearch

        detailSearchViewModel.getAllProduct(keySearch)
    }

    /**
     * translation screen
     */
    private fun transitToLoginScreen() {
        val action = ProductFragmentDirections.actionBottomNavProductToLoginFragment()
        findNavController().navigate(action)
    }

    /**
     * onItemClick listener
     */
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

        addProductDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        addProductDialog.show()
        addProductDialog.window?.setGravity(Gravity.CENTER)
        addProductDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addProductDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun clickAddProduct(number: Int) {
        isClickAddProduct = true
        Log.d("CategoryFragment", "number $number; product id $productId")

        val body = ProductBody(productId, number)
        detailSearchViewModel.addProductToCart(body)
    }

}