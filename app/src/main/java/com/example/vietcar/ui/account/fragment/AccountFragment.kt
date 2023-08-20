package com.example.vietcar.ui.account.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.R
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.data.model.account.AccountScreenCategory
import com.example.vietcar.databinding.FragmentAccountBinding
import com.example.vietcar.ui.account.adapter.AccountAdapter


class AccountFragment : BaseFragment<FragmentAccountBinding>(
    FragmentAccountBinding::inflate
) {

    private var accountAdapter = AccountAdapter()
    private var listCategory = arrayListOf(
        AccountScreenCategory(0, R.drawable.ic_account, "Thông tin cá nhân"),
        AccountScreenCategory(0, R.drawable.ic_qr_code, "Mã giới thiệu"),
        AccountScreenCategory(0, R.drawable.ic_order_history, "Lịch sử đơn hàng"),
        AccountScreenCategory(0, R.drawable.ic_address, "Sổ địa chỉ"),
        AccountScreenCategory(0, R.drawable.ic_qr_code, "Cây giới thiệu"),
        AccountScreenCategory(0, R.drawable.ic_introduce, "Giới thiệu ứng dụng"),
        AccountScreenCategory(0, R.drawable.ic_policy, "Chính sách và điều khoản"),
        AccountScreenCategory(0, R.drawable.ic_password, "Đổi mật khẩu"),
        AccountScreenCategory(0, R.drawable.ic_contact, "Liên hệ"),
        AccountScreenCategory(0, R.drawable.ic_log_out, "Đăng xuất"),
        AccountScreenCategory(0, R.drawable.ic_log_out, "Xóa tài khoản"),
    )

    override fun initData() {
        super.initData()

    }

    override fun initView() {
        super.initView()

        accountAdapter.differ.submitList(listCategory)
        binding.rvAccountScreen.adapter = accountAdapter
        binding.rvAccountScreen.layoutManager = LinearLayoutManager(requireContext())
    }

}