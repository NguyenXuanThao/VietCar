package com.example.vietcar.ui.address.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.data.model.address.Address
import com.example.vietcar.databinding.ItemAddressLayoutBinding

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private var binding: ItemAddressLayoutBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        binding =
            ItemAddressLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(differ.currentList[position])

        if (position == itemCount - 1) {
            binding!!.tvDefault.visibility = View.VISIBLE
        }

    }

    inner class AddressViewHolder(private val binding: ItemAddressLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address) {
            binding.tvName.text = address.customer_name
            binding.tvPhone.text = address.customer_phone
            binding.tvAddress.text = address.address
        }
    }

}