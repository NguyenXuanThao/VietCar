package com.example.vietcar.ui.address.fragment


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
import com.example.vietcar.databinding.FragmentAddressBinding
import com.example.vietcar.ui.address.adapter.AddressAdapter
import com.example.vietcar.ui.address.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressFragment : BaseFragment<FragmentAddressBinding>(
    FragmentAddressBinding::inflate
) {

    private val addressAdapter = AddressAdapter()

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
                    binding.rvAddress.adapter = addressAdapter
                    binding.rvAddress.layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
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

        binding.btnAddAddress.setOnClickListener {
            val action = AddressFragmentDirections.actionAddressFragmentToLocationFragment()
            findNavController().navigate(action)
        }

    }

}