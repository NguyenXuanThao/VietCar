package com.example.vietcar.ui.store_detail.fragment


import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.store_detail.StoreDetailBody
import com.example.vietcar.databinding.FragmentStoreDetailBinding
import com.example.vietcar.ui.store_detail.viewmodel.StoreDetailViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StoreDetailFragment : BaseFragment<FragmentStoreDetailBinding>(
    FragmentStoreDetailBinding::inflate
) {

    private lateinit var frameLayout: FrameLayout

    private val storeDetailViewModel: StoreDetailViewModel by viewModels()

    private val args: StoreDetailFragmentArgs by navArgs()

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        storeDetailViewModel.storeDetailResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {

                    val storeDetail = resource.data!!

                    if (storeDetail.status == 0) {

                        binding.tvName.text = storeDetail.data!!.title.toString()
                        binding.tvAddress.text = storeDetail.data.address.toString()
                        binding.tvClock.text =
                            "Giờ mở cửa ${storeDetail.data.dateform} đến ${storeDetail.data.dateto}"

                        val uriImage = "https://vietcargroup.com${storeDetail.data.url_image}"
                        Glide.with(requireContext()).load(uriImage)
                            .placeholder(R.drawable.img_default)
                            .into(binding.imgStore)

                        webViewDetail(storeDetail.data.content)
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

        val id = args.id

        Log.d("StoreDetail", id.toString())

        val body = StoreDetailBody(id)

        storeDetailViewModel.getStoreDetail(body)
    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun evenClick() {
        super.evenClick()

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    /**
     * show Info product to web view
     */
    @SuppressLint("SetJavaScriptEnabled", "ResourceAsColor")
    private fun webViewDetail(content: String?) {
        binding.webViewStoreDetail.setBackgroundColor(R.color.PriceColor)
        binding.webViewStoreDetail.webViewClient = object : WebViewClient() {

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

                binding.webViewStoreDetail.evaluateJavascript(jsCode, null)
            }
        }

        binding.webViewStoreDetail.settings.apply {
            javaScriptEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        if (content != null) {

            binding.webViewStoreDetail.loadData(content, "text/html", "UTF-8")
        }
    }

}