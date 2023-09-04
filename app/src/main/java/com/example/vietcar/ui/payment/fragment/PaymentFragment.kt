package com.example.vietcar.ui.payment.fragment

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.databinding.FragmentPaymentBinding
import com.example.vietcar.ui.payment.adapter.PaymentAdapter
import com.example.vietcar.ui.payment.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentFragment : BaseFragment<FragmentPaymentBinding>(
    FragmentPaymentBinding::inflate
) {

    private val paymentViewModel: PaymentViewModel by viewModels()

    private val paymentAdapter = PaymentAdapter()

    private lateinit var frameLayout: FrameLayout

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        paymentViewModel.productResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    paymentAdapter.differ.submitList(resource.data?.data)

                    binding.rvPayment.adapter = paymentAdapter
                    binding.rvPayment.layoutManager = LinearLayoutManager(requireContext())
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

        paymentViewModel.getProductShoppingCart()
    }

    override fun evenClick() {
        super.evenClick()

        binding.ctDeliveryMethod.setOnClickListener {
            val action = PaymentFragmentDirections.actionPaymentFragmentToDeliveryMethodFragment()
            findNavController().navigate(action)
        }

        binding.ctAddress.setOnClickListener {
            val action = PaymentFragmentDirections.actionPaymentFragmentToAddressFragment()
            findNavController().navigate(action)
        }

        binding.ctPaymentMethod.setOnClickListener {
            val action = PaymentFragmentDirections.actionPaymentFragmentToPaymentMethodFragment()
            findNavController().navigate(action)
        }

        binding.ctDiscount.setOnClickListener {
            val action = PaymentFragmentDirections.actionPaymentFragmentToDiscountCodeFragment()
            findNavController().navigate(action)
        }

    }
}