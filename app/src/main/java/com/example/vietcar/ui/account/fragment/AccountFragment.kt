package com.example.vietcar.ui.account.fragment

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.base.dialogs.ConfirmDialog
import com.example.vietcar.click.LogOutAccount
import com.example.vietcar.common.DataLocal
import com.example.vietcar.common.Resource
import com.example.vietcar.common.Utils
import com.example.vietcar.data.model.account.AccountScreenCategory
import com.example.vietcar.databinding.FragmentAccountBinding
import com.example.vietcar.ui.account.adapter.AccountAdapter
import com.example.vietcar.ui.account.viewmodel.AccountViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(
    FragmentAccountBinding::inflate
), LogOutAccount, ConfirmDialog.ConfirmCallback {

    private var accountAdapter = AccountAdapter(this)
    private var listCategory = arrayListOf(
        AccountScreenCategory(5, R.drawable.ic_introduce, "Giới thiệu ứng dụng"),
        AccountScreenCategory(6, R.drawable.ic_policy, "Chính sách và điều khoản"),
        AccountScreenCategory(8, R.drawable.ic_contact, "Liên hệ"),
    )

    private val accountViewModel: AccountViewModel by viewModels()

    private lateinit var frameLayout: FrameLayout

    private lateinit var bottomNavigationView: BottomNavigationView

    private fun setVisibility(isLoading: Boolean) {
        frameLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun obServerLivedata() {
        super.obServerLivedata()

        frameLayout = requireActivity().findViewById(R.id.frameLayout)

        accountViewModel.accountInformationResponse.observe(viewLifecycleOwner) { resource ->

            setVisibility(resource is Resource.Loading)

            when (resource) {
                is Resource.Success -> {
                    val accountInformation = resource.data?.data

                    binding.tvName.text = accountInformation?.name
                    binding.tvPhoneNumber.text = accountInformation?.phone
                    val uriImage = "https://vietcargroup.com${accountInformation?.image}"
                    Glide.with(requireContext()).load(uriImage).error(R.drawable.ic_user)
                        .into(binding.imgAvatar)
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

        if (DataLocal.STATUS == 0) {

            accountViewModel.getAccountInformation()

            updateInfo()
        }

    }

    override fun initView() {
        super.initView()

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        bottomNavigationView.visibility = View.VISIBLE

        accountAdapter.differ.submitList(listCategory)
        binding.rvAccountScreen.adapter = accountAdapter
        binding.rvAccountScreen.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun evenClick() {
        super.evenClick()

        binding.cvEditFile.setOnClickListener {
            val action = AccountFragmentDirections.actionBottomNavAccountToUpdateInfoFragment()
            findNavController().navigate(action)
        }

        binding.btnLogin.setOnClickListener {

            transitToLoginScreen()
        }
    }

    private fun updateInfo() {
        binding.btnLogin.visibility = View.GONE
        binding.tvName.visibility = View.VISIBLE
        binding.tvPhoneNumber.visibility = View.VISIBLE
        binding.cvEditFile.visibility = View.VISIBLE

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

    override fun logOut() {

        Utils.showDialogConfirm(requireContext(), "Bạn muốn đăng xuất!", this)
    }

    private fun saveData() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("status_key", 1)

        editor.apply()
    }

    override fun onClickConfirm() {
        saveData()

        val action = AccountFragmentDirections.actionBottomNavAccountSelf()
        findNavController().navigate(action)
    }

}