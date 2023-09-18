package com.example.vietcar.ui.payment_method.fragment


import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.databinding.FragmentPaymentMethodBinding


class PaymentMethodFragment : BaseFragment<FragmentPaymentMethodBinding>(
    FragmentPaymentMethodBinding::inflate
) {

    override fun evenClick() {
        super.evenClick()

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvCash.setOnClickListener {
            backToPaymentScreen(0)
        }

        binding.tvBank.setOnClickListener {
            backToPaymentScreen(1)
        }

        binding.tvPoint.setOnClickListener {
            backToPaymentScreen(2)
        }
    }

    private fun backToPaymentScreen(method: Int) {

        val args = bundleOf("paymentMethod" to method)

        setFragmentResult("paymentMethodResult", args)

        findNavController().popBackStack()
    }
}