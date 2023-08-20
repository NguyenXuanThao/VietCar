package com.example.vietcar.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vietcar.data.model.product.ProductData
import com.example.vietcar.databinding.ItemProductBinding

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var binding: ItemProductBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<ProductData>() {
        override fun areItemsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
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

        fun bind(productData: ProductData) {
            binding.tvNameProduct.text = productData.name
            val price = String.format("%,d đ", productData.total_price.toLong())
            binding.tvPriceProduct.text = price

            val uriImage = "https://vietcargroup.com${productData.avatar}"
            Glide.with(itemView.context).load(uriImage)
                .into(binding.imgProduct)
        }
    }
}