package com.example.vietcar.ui.shopping_cart.fragment

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.click.ItemProductOfCartClick
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.data.model.product.ProductOfCartBody
import com.example.vietcar.databinding.FragmentShoppingCartBinding
import com.example.vietcar.ui.shopping_cart.adapter.ShoppingCartAdapter
import com.example.vietcar.ui.shopping_cart.viewmodel.ShoppingCartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingCartFragment : BaseFragment<FragmentShoppingCartBinding>(
    FragmentShoppingCartBinding::inflate
), ItemProductOfCartClick {

    private lateinit var bottomNavigationView: BottomNavigationView

    private val shoppingCartViewModel: ShoppingCartViewModel by viewModels()

    private var shoppingCartAdapter = ShoppingCartAdapter(this)

    private var totalMoney = 0

    private var listProduct: ArrayList<Product>? = ArrayList()

    override fun onResume() {
        super.onResume()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun obServerLivedata() {
        super.obServerLivedata()

        shoppingCartViewModel.productResponse.observe(viewLifecycleOwner) { products ->

            listProduct = products.data as ArrayList<Product>

            shoppingCartAdapter.differ.submitList(products.data)

            binding.rvProductFragment.adapter = shoppingCartAdapter
            binding.rvProductFragment.layoutManager = LinearLayoutManager(requireContext())

            totalMoney(products.data)
        }

        shoppingCartViewModel.productOfCartResponse.observe(viewLifecycleOwner) { productToCart ->


        }
    }

    override fun initData() {
        super.initData()

        shoppingCartViewModel.getProductShoppingCart()
    }

    private fun totalMoney(listProduct: ArrayList<Product>) {
        for (product in listProduct) {
            val netPrice = product.net_price!! * product.quantity_buy!!
            totalMoney += netPrice
        }

        val convertTotalMoney = totalMoney.let { String.format("%,d đ", it.toLong()) }

        binding.tvTotal.text = convertTotalMoney
    }

    override fun increase(product: Product) {

        Log.d("ThaoNX", product.quantity_buy.toString())

        val body = ProductOfCartBody(product.cart_id!!, product.quantity_buy!!)

        shoppingCartViewModel.updateQuantity(body)

        val totalMoney = binding.tvTotal.text.toString().replace("[^0-9]".toRegex(), "").toLong()
            .toInt() + product.net_price!!

        val price = totalMoney.let { String.format("%,d đ", it.toLong()) }

        binding.tvTotal.text = price

    }

    override fun decrease(product: Product) {
        Log.d("ThaoNX", product.quantity_buy.toString())

        val body = ProductOfCartBody(product.cart_id!!, product.quantity_buy!!)

        shoppingCartViewModel.updateQuantity(body)

        val totalMoney =
            binding.tvTotal.text.toString().replace("[^0-9]".toRegex(), "").toLong()
                .toInt() - product.net_price!!

        val price = totalMoney.let { String.format("%,d đ", it.toLong()) }

        binding.tvTotal.text = price
    }

    override fun delete(product: Product) {

        shoppingCartViewModel.deleteProductOfCart(product.cart_id!!)

        val totalMoney =
            binding.tvTotal.text.toString().replace("[^0-9]".toRegex(), "").toLong()
                .toInt() - product.net_price!! * product.quantity_buy!!

        val price = totalMoney.let { String.format("%,d đ", it.toLong()) }

        binding.tvTotal.text = price

        listProduct!!.remove(product)

        shoppingCartAdapter.differ.submitList(listProduct)

        binding.rvProductFragment.adapter = shoppingCartAdapter
        binding.rvProductFragment.layoutManager = LinearLayoutManager(requireContext())

    }

}