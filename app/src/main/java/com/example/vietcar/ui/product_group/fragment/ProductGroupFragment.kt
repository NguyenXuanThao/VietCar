package com.example.vietcar.ui.product_group.fragment

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
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.example.vietcar.databinding.FragmentProductGroupBinding
import com.example.vietcar.ui.product.adapter.ProductAdapter
import com.example.vietcar.ui.product_group.viewmodel.ProductGroupViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductGroupFragment : BaseFragment<FragmentProductGroupBinding>(
    FragmentProductGroupBinding::inflate
), ItemShoppingCartClick, ConfirmDialog.ConfirmCallback, AddProductDialog.AddProductCallBack {

    private val productAdapter = ProductAdapter(this)

    private var groupId: String? = null

    private var groupTitle: String? = null

    private val productGroupViewModel: ProductGroupViewModel by viewModels()

    private val args: ProductGroupFragmentArgs by navArgs()

    private lateinit var bottomNavigationView: BottomNavigationView

    private var productId = 0

    private var isClickAddProduct = false

    private lateinit var frameLayout: FrameLayout

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        groupId = args.groupId

        groupTitle = args.groupTitle

        productGroupViewModel.productResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.tvTitle.text = groupTitle

                    productAdapter.differ.submitList(resource.data?.data)
                    binding.rvProductGroupFragment.adapter = productAdapter
                    binding.rvProductGroupFragment.layoutManager =
                        GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

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

        productGroupViewModel.productToCartResponse.observe(viewLifecycleOwner) { resource ->
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

        groupId?.let { productGroupViewModel.getListProductGroup(it) }

    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun evenClick() {
        super.evenClick()

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgSearchProduct.setOnClickListener {
            val action = ProductGroupFragmentDirections.actionProductGroupFragmentToSearchFragment()
            findNavController().navigate(action)
        }
    }

    /**
     * translation screen
     */
    private fun transitToLoginScreen() {
        val action = ProductGroupFragmentDirections.actionProductGroupFragmentToLoginFragment()
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
     * confirm dialog
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
        Log.d("CategoryFragment", "number $number; product id $productId")

        val body = ProductBody(productId, number)
        productGroupViewModel.addProductToCart(body)
    }

}