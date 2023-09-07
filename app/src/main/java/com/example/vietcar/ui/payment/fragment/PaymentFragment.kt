package com.example.vietcar.ui.payment.fragment

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.address.Address
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

    private var hasAddressData = false

    private var addressData: Address? = null

    private var deliveryMethod: Int? = null
    private var paymentMethod: Int? = null

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

        paymentViewModel.addressResponse.observe(viewLifecycleOwner) { resource ->
            if (!hasAddressData) {
                when (resource) {
                    is Resource.Success -> {
                        if (resource.data?.data!!.isNotEmpty()) {
                            binding.tvAddress.visibility = View.GONE
                            binding.tvName.visibility = View.VISIBLE
                            binding.tvPhone.visibility = View.VISIBLE
                            binding.tvAddressDefault.visibility = View.VISIBLE

                            val lastAddress = resource.data.data.last()

                            binding.tvName.text = lastAddress.customer_name
                            binding.tvPhone.text = "SĐT ${lastAddress?.customer_phone}"
                            binding.tvAddressDefault.text = lastAddress.address
                        }
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
    }

    override fun initData() {
        super.initData()

        paymentViewModel.getProductShoppingCart()

        paymentViewModel.getAddress()
    }

    override fun initView() {
        super.initView()

        checkDeliveryMethod()

        checkPaymentMethod()

        checkAddressResult()
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

    private fun checkPaymentMethod() {

        parentFragmentManager.setFragmentResultListener(
            "paymentMethodResult",
            viewLifecycleOwner
        ) { _, result ->

            paymentMethod = result.getInt("paymentMethod")

            savePaymentMethod()
        }

        savePaymentMethod()
    }

    private fun checkDeliveryMethod() {

        parentFragmentManager.setFragmentResultListener(
            "deliveryMethodResult",
            viewLifecycleOwner
        ) { _, result ->

            deliveryMethod = result.getInt("deliveryMethod")

            showHideAddress()
        }

        showHideAddress()
    }

    private fun checkAddressResult() {
        parentFragmentManager.setFragmentResultListener(
            "addressResult",
            viewLifecycleOwner
        ) { _, result ->
            addressData = result.getParcelable("addressData")

            showHideAddressDefault()

        }

        if (addressData != null) {

            showHideAddressDefault()
        }
    }

    private fun showHideAddressDefault() {
        binding.tvAddress.visibility = View.GONE
        binding.tvName.visibility = View.VISIBLE
        binding.tvPhone.visibility = View.VISIBLE
        binding.tvAddressDefault.visibility = View.VISIBLE

        binding.tvName.text = addressData?.customer_name
        binding.tvPhone.text = "SĐT ${addressData?.customer_phone}"
        binding.tvAddressDefault.text = addressData?.address

        hasAddressData = true
    }

    private fun showHideAddress() {

        when (deliveryMethod) {
            0 -> {
                binding.tvDeliveryMethod.text = "Giao hàng tận nơi"
                binding.ctAddress.visibility = View.VISIBLE
                binding.ctAddressDefault.visibility = View.VISIBLE
                binding.view3.visibility = View.VISIBLE
            }

            1 -> {
                binding.tvDeliveryMethod.text = "Nhận tại cửa hàng"
                binding.ctAddress.visibility = View.GONE
                binding.ctAddressDefault.visibility = View.GONE
                binding.view3.visibility = View.GONE
            }

            else -> binding.tvDeliveryMethod.text = "Giao hàng tận nơi"
        }
    }

    private fun savePaymentMethod() {
        when (paymentMethod) {
            0 -> {
                binding.tvPaymentMethod.text = "Tiền mặt"
            }

            1 -> {
                binding.tvPaymentMethod.text = "Chuyển khoản"
            }

            2 -> {
                binding.tvPaymentMethod.text = "Ví điểm"
            }

            else -> binding.tvPaymentMethod.text = "Tiền mặt"
        }
    }
}