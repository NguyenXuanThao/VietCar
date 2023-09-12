package com.example.vietcar.ui.payment.fragment

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.activities.MainActivity
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.AddProductDialog
import com.example.vietcar.base.dialogs.SuccessDialog
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.address.Address
import com.example.vietcar.data.model.bill.BillBody
import com.example.vietcar.data.model.bill.BillResponse
import com.example.vietcar.data.model.bill_detail.OrderBody
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.databinding.FragmentPaymentBinding
import com.example.vietcar.ui.payment.adapter.PaymentAdapter
import com.example.vietcar.ui.payment.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentFragment : BaseFragment<FragmentPaymentBinding>(
    FragmentPaymentBinding::inflate
), SuccessDialog.TransitToOtherScreen {

    companion object {
        const val CHANEL_ID = "OrderChannel"
    }


    private val paymentViewModel: PaymentViewModel by viewModels()

    private val paymentAdapter = PaymentAdapter()

    private lateinit var frameLayout: FrameLayout

    private var hasAddressData = false

    private var billId = -1
    private var note: String? = null
    private var addressId = -1

    private var billResponse: BillResponse? = null

    private val args: PaymentFragmentArgs by navArgs()

    private var addressData: Address? = null

    private var deliveryMethod: Int? = null
    private var paymentMethod: Int? = null

    private var billCode: String? = null

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

        paymentViewModel.billResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {

                    if (resource.data?.status == 0) {

                        sendNotification()

                        showDialogSuccess()
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

    override fun initData() {
        super.initData()

        paymentViewModel.getProductShoppingCart()

        paymentViewModel.getAddress()

        orderProduct()

    }

    override fun initView() {
        super.initView()

        checkDeliveryMethod()

        checkPaymentMethod()

        checkAddressResult()
    }

    override fun evenClick() {
        super.evenClick()

        binding.btnOrder.setOnClickListener {

            if (paymentMethod != 0 && paymentMethod != 1 && paymentMethod != 2) {

                Toast.makeText(
                    requireContext(),
                    "Vui lòng chọn phương thức thanh toán",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val body = OrderBody(billId, addressId, deliveryMethod, note, paymentMethod)

                Log.d("ThaoNX6", body.toString())

                paymentViewModel.orderProduct(body)
            }
        }



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

            Log.d("ThaoNX6", addressData.toString())

            addressId = addressData?.id!!

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

        addressId = addressData?.id!!

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

            else -> binding.tvPaymentMethod.text = "Chọn phương thức thanh toán"
        }
    }

    private fun orderProduct() {
        billResponse = args.bill

        billCode = billResponse?.data?.code

        billId = billResponse?.data?.id!!
        note = billResponse?.data?.note
        addressId = billResponse?.data?.delivery_address_id!!
        deliveryMethod = billResponse?.data?.delivery_method
    }

    @SuppressLint("RemoteViewLayout", "MissingPermission")
    private fun sendNotification() {

        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.action = "OPEN_FRAGMENT_HISTORY"

        val pendingIntent =
            PendingIntent.getActivity(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val customLayout = RemoteViews(requireContext().packageName, R.layout.custom_notify)
        customLayout.setTextViewText(R.id.tvNotificationName, "Thông báo đơn hàng")
        customLayout.setTextViewText(R.id.tvNotificationContent, "Đơn hàng được đặt thành công")
        customLayout.setImageViewResource(R.id.ivNotificationImage, R.drawable.ic_splash)

        customLayout.setViewPadding(R.id.ivNotificationImage, 4, 4, 4, 4)
        customLayout.setImageViewBitmap(
            R.id.ivNotificationImage,
            BitmapFactory.decodeResource(resources, R.drawable.ic_splash)
        )

        val notification = NotificationCompat.Builder(requireContext(), CHANEL_ID)
            .setSmallIcon(R.drawable.ic_splash)
            .setContent(customLayout)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(requireContext())

        notificationManager.notify(1, notification)
    }

    private fun showDialogSuccess() {
        val successDialog = SuccessDialog(
            this,
            requireContext(),
            "Đặt đơn hàng thành công"
        )
        successDialog.show()
        successDialog.window?.setGravity(Gravity.CENTER)
        successDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun clickSwitchScreen() {

        if (paymentMethod == 1) {
            transitToBankAccountScreen()
        } else {
            transitToHomeScreen()
        }
    }

    private fun transitToHomeScreen() {
        val action = PaymentFragmentDirections.actionPaymentFragmentToBottomNavHome()
        findNavController().navigate(action)
    }

    private fun transitToBankAccountScreen() {
        val action =
            PaymentFragmentDirections.actionPaymentFragmentToBankAccountFragment(billCode!!)
        findNavController().navigate(action)
    }
}