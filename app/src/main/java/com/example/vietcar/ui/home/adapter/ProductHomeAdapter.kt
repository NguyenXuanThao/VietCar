package com.example.vietcar.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.databinding.ItemProductBinding

class ProductHomeAdapter : RecyclerView.Adapter<ProductHomeAdapter.ProductViewHolder>() {

    private var binding: ItemProductBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvNameProduct.text = product.name
            val price = product.total_price?.let { String.format("%,d đ", it.toLong()) }
            binding.tvPriceProduct.text = price

            val uriImage = "https://vietcargroup.com${product.avatar}"
            Glide.with(itemView.context).load(uriImage)
                .into(binding.imgProduct)
        }
    }
}