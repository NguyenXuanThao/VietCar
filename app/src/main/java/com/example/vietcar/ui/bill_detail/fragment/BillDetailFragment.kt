package com.example.vietcar.ui.bill_detail.fragment


import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.bill_detail.BillDetail
import com.example.vietcar.databinding.FragmentBillDetailBinding
import com.example.vietcar.ui.bill_detail.adapter.BillDetailAdapter
import com.example.vietcar.ui.bill_detail.viewmodel.BillDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillDetailFragment : BaseFragment<FragmentBillDetailBinding>(
    FragmentBillDetailBinding::inflate
) {

    private val billDetailViewModel: BillDetailViewModel by viewModels()

    private val args: BillDetailFragmentArgs by navArgs()

    private var billDetailAdapter = BillDetailAdapter()

    private lateinit var frameLayout: FrameLayout

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        billDetailViewModel.billDetailResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {

                    val billDetail = resource.data!!

                    updateView(billDetail)

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

        val billId = args.id

        billDetailViewModel.getBillDetail(billId)
    }

    private fun updateView(billDetail: BillDetail) {

        billDetailAdapter.differ.submitList(billDetail.data.bill_product)

        binding.rvProduct.adapter = billDetailAdapter
        binding.rvProduct.layoutManager = LinearLayoutManager(requireContext())

        binding.tvCode.text = billDetail.data.code
        binding.tvDate.text = billDetail.data.created_at
        if (billDetail.data.delivery_method == 0) {
            binding.tvDelivery.text = "Giao hàng tận nơi"
        } else {
            binding.tvDelivery.text = "Nhận tại cửa hàng"
        }
        binding.tvPhone.text = billDetail.data.customer_phone

        when (billDetail.data.payment_method) {
            0 -> {
                binding.tvPayment.text = "Tiền mặt"
            }

            1 -> {
                binding.tvPayment.text = "Chuyển khoản"
            }

            2 -> {
                binding.tvPayment.text = "Ví điểm"
            }

            else -> binding.tvPayment.text = "Tiền mặt"
        }

        val price = billDetail.data.net_price?.let { String.format("%,d đ", it.toLong()) }
        binding.tvNEstimate.text = price
        binding.tvTotal.text = price
    }

}