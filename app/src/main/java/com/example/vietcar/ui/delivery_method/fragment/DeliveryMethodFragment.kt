package com.example.vietcar.ui.delivery_method.fragment


import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.databinding.FragmentDeliveryMethodBinding


class DeliveryMethodFragment : BaseFragment<FragmentDeliveryMethodBinding>(
    FragmentDeliveryMethodBinding::inflate
) {

    override fun evenClick() {
        super.evenClick()

        binding.tvDelivery.setOnClickListener {
            backToPaymentScreen(0)
        }

        binding.tvAtStore.setOnClickListener {
            backToPaymentScreen(1)
        }


    }

    private fun backToPaymentScreen(method: Int) {

        val args = bundleOf("deliveryMethod" to method)

        setFragmentResult("deliveryMethodResult", args)

        findNavController().popBackStack()
    }

}