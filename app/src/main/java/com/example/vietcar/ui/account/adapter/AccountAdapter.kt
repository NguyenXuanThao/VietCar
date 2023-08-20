package com.example.vietcar.ui.account.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.data.model.account.AccountScreenCategory
import com.example.vietcar.databinding.ItemAccountScreenBinding

class AccountAdapter: RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    private var binding: ItemAccountScreenBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<AccountScreenCategory>() {
        override fun areItemsTheSame(oldItem: AccountScreenCategory, newItem: AccountScreenCategory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AccountScreenCategory, newItem: AccountScreenCategory): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        binding =
            ItemAccountScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class AccountViewHolder(private val binding: ItemAccountScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(accountScreenCategory: AccountScreenCategory) {

            binding.tvTitle.text = accountScreenCategory.title
            binding.imgIcon.setImageResource(accountScreenCategory.image)
        }
    }
}