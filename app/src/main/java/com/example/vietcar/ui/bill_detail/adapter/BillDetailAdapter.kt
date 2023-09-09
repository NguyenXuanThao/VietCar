package com.example.vietcar.ui.bill_detail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vietcar.data.model.bill_detail.BillProduct
import com.example.vietcar.databinding.ItemProductPaymentBinding

class BillDetailAdapter : RecyclerView.Adapter<BillDetailAdapter.BillDetailViewHolder>(){

    private var binding: ItemProductPaymentBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<BillProduct>() {
        override fun areItemsTheSame(oldItem: BillProduct, newItem: BillProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BillProduct, newItem: BillProduct): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillDetailViewHolder {
        binding =
            ItemProductPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillDetailViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BillDetailViewHolder, position: Int) {
        val currentList = differ.currentList
        holder.bind(currentList[position])

    }

    inner class BillDetailViewHolder(private val binding: ItemProductPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(billProduct: BillProduct) {
            binding.tvNameProduct.text = billProduct.product_name
            val price = billProduct.price?.let { String.format("%,d đ", it.toLong()) }
            binding.tvPriceProduct.text = price
            binding.tvQuantity.text = "Số lượng: ${billProduct.quantity}"

            val uriImage = "https://vietcargroup.com${billProduct.product_avatar}"
            Glide.with(itemView.context).load(uriImage)
                .into(binding.imgProduct)
        }
    }
}