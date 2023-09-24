package com.example.vietcar.ui.address.fragment


import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.click.ItemAddressClick
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.address.Address
import com.example.vietcar.databinding.FragmentAddressBinding
import com.example.vietcar.ui.address.adapter.AddressAdapter
import com.example.vietcar.ui.address.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressFragment : BaseFragment<FragmentAddressBinding>(
    FragmentAddressBinding::inflate
), ItemAddressClick {

    private val addressAdapter = AddressAdapter(this)

    private val addressViewModel: AddressViewModel by viewModels()

    private lateinit var frameLayout: FrameLayout
    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        addressViewModel.addressResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    addressAdapter.differ.submitList(resource.data?.data)

                    Log.d("obServerLivedata", resource.data?.data.toString())
                    binding.rvAddress.adapter = addressAdapter
                    binding.rvAddress.layoutManager =
                        LinearLayoutManager(requireContext())
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

        addressViewModel.getAddress()
    }

    override fun evenClick() {
        super.evenClick()

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnAddAddress.setOnClickListener {
            val action = AddressFragmentDirections.actionAddressFragmentToLocationFragment()
            findNavController().navigate(action)
        }

    }

    override fun onClickAddressItem(address: Address) {
        backToPaymentScreen(address)
    }

    private fun backToPaymentScreen(address: Address) {

        val args = bundleOf("addressData" to address)

        setFragmentResult("addressResult", args)

        findNavController().popBackStack()
    }

}