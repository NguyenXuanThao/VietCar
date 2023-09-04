package com.example.vietcar.ui.payment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.databinding.ItemProductPaymentBinding

class PaymentAdapter : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    private var binding: ItemProductPaymentBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        binding =
            ItemProductPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val currentList = differ.currentList
        holder.bind(currentList[position])

    }

    inner class PaymentViewHolder(private val binding: ItemProductPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            binding.tvNameProduct.text = product.name
            val price = product.net_price?.let { String.format("%,d đ", it.toLong()) }
            binding.tvPriceProduct.text = price
            binding.tvQuantity.text = "Số lượng: ${product.quantity_buy}"

            val uriImage = "https://vietcargroup.com${product.avatar}"
            Glide.with(itemView.context).load(uriImage)
                .into(binding.imgProduct)
        }
    }
}