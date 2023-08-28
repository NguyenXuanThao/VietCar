package com.example.vietcar.ui.product.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.click.ItemShoppingCartClick
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.databinding.FragmentProductBinding
import com.example.vietcar.ui.product.adapter.ProductAdapter
import com.example.vietcar.ui.product.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@AndroidEntryPoint
class ProductFragment : BaseFragment<FragmentProductBinding>(
    FragmentProductBinding::inflate
), ItemShoppingCartClick {

    private val productViewModel: ProductViewModel by viewModels()
    private var productAdapter = ProductAdapter(this)

    override fun obServerLivedata() {
        productViewModel.productResponse.observe(viewLifecycleOwner) { products ->
            productAdapter.differ.submitList(products.data)
            binding.rvProductFragment.adapter = productAdapter
            binding.rvProductFragment.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    override fun initData() {
        productViewModel.getAllProduct()
    }

    override fun onItemClick(product: Product) {
        TODO("Not yet implemented")
    }

}