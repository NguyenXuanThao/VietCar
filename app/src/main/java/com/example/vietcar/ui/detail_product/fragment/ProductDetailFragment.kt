package com.example.vietcar.ui.detail_product.fragment

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.databinding.FragmentProductDetailBinding
import com.example.vietcar.ui.detail_product.viewmodel.ProductDetailViewModel
import com.example.vietcar.ui.product.adapter.ProductAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>(
    FragmentProductDetailBinding::inflate
) {

    private var product: Product? = null

    private val args: ProductDetailFragmentArgs by navArgs()

    private val productAdapter = ProductAdapter()

    private val productDetailViewModel: ProductDetailViewModel by viewModels()

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onDestroy() {
        super.onDestroy()

//        findNavController().addOnDestinationChangedListener {_, destination, _ ->
//            if (destination.id == R.id.detailProductFragment) {

//                if (findNavController().previousBackStackEntry?.destination?.id == R.id.bottomNavHome) {
                    bottomNavigationView.visibility = View.VISIBLE
//                }
//            }
//        }
    }

    override fun obServerLivedata() {
        super.obServerLivedata()

        productDetailViewModel.relatedProductResponse.observe(viewLifecycleOwner) { products ->

            productAdapter.differ.submitList(products.data)
            binding.rvRelatedProducts.adapter = productAdapter
            binding.rvRelatedProducts.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }
    }

    override fun initData() {
        super.initData()

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
}