package com.example.vietcar.ui.confirm_order.fragment


import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.databinding.FragmentOrderConfirmBinding
import com.example.vietcar.ui.confirm_order.adapter.OrderConfirmAdapter
import com.example.vietcar.ui.confirm_order.viewmodel.OrderConfirmViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderConfirmFragment : BaseFragment<FragmentOrderConfirmBinding>(
    FragmentOrderConfirmBinding::inflate
) {

    private val orderConfirmViewModel: OrderConfirmViewModel by viewModels()

    private var orderConfirmAdapter = OrderConfirmAdapter()


    override fun obServerLivedata() {
        super.obServerLivedata()

        orderConfirmViewModel.billResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    orderConfirmAdapter.differ.submitList(resource.data?.data)
                    binding.rvOrderConfirm.adapter = orderConfirmAdapter
                    binding.rvOrderConfirm.layoutManager =
                        LinearLayoutManager(requireContext())
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {
                }
            }
        }
    }

    override fun initData() {
        super.initData()

        orderConfirmViewModel.getListBill()
    }

}