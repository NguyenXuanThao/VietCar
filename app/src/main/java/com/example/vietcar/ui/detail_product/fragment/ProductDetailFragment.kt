package com.example.vietcar.ui.detail_product.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.AddProductDialog
import com.example.vietcar.base.dialogs.ConfirmDialog
import com.example.vietcar.click.ItemShoppingCartClick
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.databinding.FragmentProductDetailBinding
import com.example.vietcar.ui.detail_product.viewmodel.ProductDetailViewModel
import com.example.vietcar.ui.product.adapter.ProductAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>(
    FragmentProductDetailBinding::inflate
), ItemShoppingCartClick, ConfirmDialog.ConfirmCallback, AddProductDialog.AddProductCallBack {

    private var product: Product? = null

    private val args: ProductDetailFragmentArgs by navArgs()

    private val productAdapter = ProductAdapter(this)

    private val productDetailViewModel: ProductDetailViewModel by viewModels()

    private lateinit var bottomNavigationView: BottomNavigationView

    private var status = 1

    private var productId = 0

    private var isClickAddProduct = false

    private lateinit var frameLayout: FrameLayout

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        productDetailViewModel.relatedProductResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    productAdapter.differ.submitList(resource.data?.data)
                    binding.rvRelatedProducts.adapter = productAdapter
                    binding.rvRelatedProducts.layoutManager =
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


        productDetailViewModel.productToCartResponse.observe(viewLifecycleOwner) { resource ->
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

        retrieveData()

        product = args.product

        productDetailViewModel.getRelatedProducts(product!!.id.toString())
    }

    @SuppressLint("SuspiciousIndentation")
    override fun initView() {
        super.initView()

        setUpView()

        webViewSortDetail()

        webViewDetail()

    }

    override fun evenClick() {
        super.evenClick()

        binding.btnAddProduct.setOnClickListener {
            if (status == 0) {
                showDialogProductInfo(product!!)
            } else {
                Utils.showDialogConfirm(requireContext(), this)
            }
        }
    }

    /**
     * translation screen
     */
    private fun transitToLoginScreen() {
        val action = ProductDetailFragmentDirections.actionDetailProductFragmentToLoginFragment()
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
    }


    private fun setUpView() {

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE

        binding.tvNameProduct.text = product!!.name

        val price = product!!.total_price?.let { String.format("%,d đ", it.toLong()) }
        binding.tvPriceProduct.text = price

        val uriImage = "https://vietcargroup.com${product!!.avatar}"
        Glide.with(requireContext()).load(uriImage)
            .into(binding.imgProduct)
    }


    /**
     * show Info product to web view
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewDetail() {
        binding.webViewDetail.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                val jsCode = """
            javascript:(function() {
                var images = document.getElementsByTagName('img');
                for (var i = 0; i < images.length; i++) {
                    images[i].style.maxWidth = '100%';
                    images[i].style.height = 'auto';
                }
                
                var paragraphs = document.getElementsByTagName('p');
                for (var i = 0; i < paragraphs.length; i++) {
                    paragraphs[i].style.fontSize = '16px';
                     paragraphs[i].style.fontFamily  = 'Arial, sans-serif';
                }
            })()
        """.trimIndent()

                binding.webViewSortDetail.evaluateJavascript(jsCode, null)
            }
        }

        binding.webViewDetail.settings.apply {
            javaScriptEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        if (product!!.description != null) {

            binding.webViewDetail.loadData(product!!.description!!, "text/html", "UTF-8")
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSortDetail() {
        binding.webViewSortDetail.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                val jsCode = """
            javascript:(function() {
                var images = document.getElementsByTagName('img');
                for (var i = 0; i < images.length; i++) {
                    images[i].style.maxWidth = '100%';
                    images[i].style.height = 'auto';
                }
                
                var paragraphs = document.getElementsByTagName('p');
                for (var i = 0; i < paragraphs.length; i++) {
                    paragraphs[i].style.fontSize = '16px';
                     paragraphs[i].style.fontFamily  = 'Arial, sans-serif';
                }
            })()
        """.trimIndent()

                binding.webViewSortDetail.evaluateJavascript(jsCode, null)
            }
        }

        binding.webViewSortDetail.settings.apply {
            javaScriptEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        if (product!!.sort_description != null) {
            binding.webViewSortDetail.loadData(product!!.sort_description!!, "text/html", "UTF-8")
        }
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
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun clickAddProduct(number: Int) {
        isClickAddProduct = true
        Log.d("CategoryFragment", "number $number; product id $productId")

        val body = ProductBody(productId, number)
        productDetailViewModel.addProductToCart(body)
    }
}