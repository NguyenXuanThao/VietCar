package com.example.vietcar.ui.bank_account.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vietcar.base.BaseFragment
import com.example.vietcar.click.ItemBankAccountClick
import com.example.vietcar.data.model.bank_account.BankAccountData
import com.example.vietcar.databinding.FragmentBankAccountBinding
import com.example.vietcar.ui.bank_account.adapter.BankAccountAdapter


class BankAccountFragment : BaseFragment<FragmentBankAccountBinding>(
    FragmentBankAccountBinding::inflate
), ItemBankAccountClick {

    private var bankAccountAdapter = BankAccountAdapter(this)

    private val args: BankAccountFragmentArgs by navArgs()

    override fun initView() {
        super.initView()

        val billCode = args.billCode

        binding.tvNoteContent.text = "Thanh toán dơn hàng $billCode"

        bankAccountAdapter.differ.submitList(BankAccountData.getData())
        binding.rvBankAccount.adapter = bankAccountAdapter
        binding.rvBankAccount.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun evenClick() {
        super.evenClick()

        binding.btnConfirm.setOnClickListener {
            val action = BankAccountFragmentDirections.actionBankAccountFragmentToBottomNavHome()
            findNavController().navigate(action)
        }

        binding.imgCopy.setOnClickListener {
            copyAccount(binding.tvNoteContent.text.toString())
        }
    }

    override fun copyAccount(accountNumber: String) {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Label", accountNumber)
        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(
            requireContext(),
            "Đã sao chép số tài khoản $accountNumber",
            Toast.LENGTH_SHORT
        )
            .show()
    }

}