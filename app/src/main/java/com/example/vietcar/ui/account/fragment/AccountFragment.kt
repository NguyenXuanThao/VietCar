package com.example.vietcar.ui.account.fragment

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.account.AccountScreenCategory
import com.example.vietcar.databinding.FragmentAccountBinding
import com.example.vietcar.ui.account.adapter.AccountAdapter
import com.example.vietcar.ui.account.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(
    FragmentAccountBinding::inflate
) {

    private var accountAdapter = AccountAdapter()
    private var listCategory = arrayListOf(
        AccountScreenCategory(5, R.drawable.ic_introduce, "Giới thiệu ứng dụng"),
        AccountScreenCategory(6, R.drawable.ic_policy, "Chính sách và điều khoản"),
        AccountScreenCategory(8, R.drawable.ic_contact, "Liên hệ"),
    )

    private val accountViewModel : AccountViewModel by viewModels()

    private lateinit var frameLayout: FrameLayout

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)
        frameLayout.visibility = View.VISIBLE

        accountViewModel.accountInformationResponse.observe(viewLifecycleOwner) { resource ->

            when (resource) {
                is Resource.Success -> {
                    binding.tvName.text = resource.data?.data?.name
                    binding.tvPhoneNumber.text = resource.data?.data?.phone

                    frameLayout.visibility = View.GONE

                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Có lỗi mạng"
                    Utils.showDialogError(requireContext(), errorMessage)
                }

                is Resource.Loading -> {
                    frameLayout.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun initData() {
        super.initData()

        accountViewModel.getAccountInformation()

        retrieveData()

    }

    override fun initView() {
        super.initView()

        accountAdapter.differ.submitList(listCategory)
        binding.rvAccountScreen.adapter = accountAdapter
        binding.rvAccountScreen.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun evenClick() {
        super.evenClick()

        binding.btnLogin.setOnClickListener {

            transitToLoginScreen()
        }
    }

    /**
     * check log in
     */

    private fun retrieveData() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val retrievedStatus = sharedPreferences.getInt("status_key", 1)


        if (retrievedStatus == 0) {

            updateInfo()
        }
    }

    private fun updateInfo() {
        binding.btnLogin.visibility = View.GONE
        binding.btnVerify.visibility = View.VISIBLE
        binding.tvPhoneNumber.visibility = View.VISIBLE
        binding.cvEditFile.visibility = View.VISIBLE
        binding.imgWarning.visibility = View.VISIBLE

        listCategory.clear()
        listCategory.addAll(
            arrayListOf(
                AccountScreenCategory(0, R.drawable.ic_account, "Thông tin cá nhân"),
                AccountScreenCategory(1, R.drawable.ic_qr_code, "Mã giới thiệu"),
                AccountScreenCategory(2, R.drawable.ic_order_history, "Lịch sử đơn hàng"),
                AccountScreenCategory(3, R.drawable.ic_address2, "Sổ địa chỉ"),
                AccountScreenCategory(4, R.drawable.ic_qr_code, "Cây giới thiệu"),
                AccountScreenCategory(5, R.drawable.ic_introduce, "Giới thiệu ứng dụng"),
                AccountScreenCategory(6, R.drawable.ic_policy, "Chính sách và điều khoản"),
                AccountScreenCategory(7, R.drawable.ic_password, "Đổi mật khẩu"),
                AccountScreenCategory(8, R.drawable.ic_contact, "Liên hệ"),
                AccountScreenCategory(9, R.drawable.ic_log_out, "Đăng xuất"),
                AccountScreenCategory(10, R.drawable.ic_log_out, "Xóa tài khoản"),
            )
        )
    }

    private fun transitToLoginScreen() {
        val action = AccountFragmentDirections.actionBottomNavAccountToLoginFragment()
        findNavController().navigate(action)
    }

}