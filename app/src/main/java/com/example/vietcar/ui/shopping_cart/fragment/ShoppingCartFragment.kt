package com.example.vietcar.ui.shopping_cart.fragment

import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.databinding.FragmentShoppingCartBinding
import com.example.vietcar.ui.shopping_cart.adapter.ShoppingCartAdapter
import com.example.vietcar.ui.shopping_cart.viewmodel.ShoppingCartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingCartFragment : BaseFragment<FragmentShoppingCartBinding>(
    FragmentShoppingCartBinding::inflate
) {

    private lateinit var bottomNavigationView: BottomNavigationView

    private val shoppingCartViewModel: ShoppingCartViewModel by viewModels()

    private var shoppingCartAdapter = ShoppingCartAdapter()

    private var totalMoney = 0

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

            shoppingCartAdapter.differ.submitList(products.data)

            for (product in products.data) {
                val netPrice = product.net_price!! * product.quantity_buy!!
                totalMoney += netPrice
            }

            val convertTotalMoney = totalMoney.let { String.format("%,d đ", it.toLong()) }

            val spannable = SpannableString("Tổng: $convertTotalMoney")

            spannable.setSpan(StyleSpan(Typeface.BOLD), 0, 5, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.tvTotal.text = spannable

            binding.rvProductFragment.adapter = shoppingCartAdapter
            binding.rvProductFragment.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun initData() {
        super.initData()

        shoppingCartViewModel.getProductShoppingCart()
    }


}