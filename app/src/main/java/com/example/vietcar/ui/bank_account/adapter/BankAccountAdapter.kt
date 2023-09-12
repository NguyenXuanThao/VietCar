package com.example.vietcar.ui.bank_account.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.click.ItemBankAccountClick
import com.example.vietcar.data.model.bank_account.BankAccount
import com.example.vietcar.databinding.ItemBankAccountBinding

class BankAccountAdapter(private val itemBankAccountClick: ItemBankAccountClick) :
    RecyclerView.Adapter<BankAccountAdapter.BankAccountViewHolder>() {

    private var binding: ItemBankAccountBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<BankAccount>() {
        override fun areItemsTheSame(oldItem: BankAccount, newItem: BankAccount): Boolean {
            return oldItem.bankName == newItem.bankName
        }

        override fun areContentsTheSame(oldItem: BankAccount, newItem: BankAccount): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankAccountViewHolder {
        binding =
            ItemBankAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BankAccountViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BankAccountViewHolder, position: Int) {
        val currentList = differ.currentList
        holder.bind(currentList[position])

    }

    inner class BankAccountViewHolder(private val binding: ItemBankAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bankAccount: BankAccount) {

            binding.tvName.text = bankAccount.bankName
            binding.tvAccountOwner.text = "Chủ TK: ${bankAccount.accountOwner}"
            binding.tvBranch.text = "Chi nhánh: ${bankAccount.branch}"
            binding.tvAccountNumber.text = "STK: ${bankAccount.accountNumber}"

            binding.imgCopy.setOnClickListener {
                itemBankAccountClick.copyAccount(bankAccount.accountNumber.toString())
            }

        }
    }
}