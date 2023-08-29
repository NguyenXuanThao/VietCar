package com.example.vietcar.ui.shopping_cart.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vietcar.click.ItemProductOfCartClick
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.databinding.ItemShoppingCartBinding

class ShoppingCartAdapter(private val itemProductOfCartClick: ItemProductOfCartClick) :
    RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder>() {

    private var binding: ItemShoppingCartBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        binding =
            ItemShoppingCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingCartViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        val currentList = differ.currentList
        holder.bind(currentList[position])

    }

    inner class ShoppingCartViewHolder(private val binding: ItemShoppingCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvNameProduct.text = product.name
            val price = product.net_price?.let { String.format("%,d đ", it.toLong()) }
            binding.tvPriceProduct.text = price
            binding.tvProductNumber.text = product.quantity_buy.toString()

            val uriImage = "https://vietcargroup.com${product.avatar}"
            Glide.with(itemView.context).load(uriImage)
                .into(binding.imgProduct)

            binding.imgIncrease.setOnClickListener {
                if (product.quantity_buy != null) {
                    product.quantity_buy = product.quantity_buy!! + 1

                    binding.tvProductNumber.text = product.quantity_buy.toString()
                    Log.d("ShoppingCartAdapter", product.quantity_buy.toString())
                    itemProductOfCartClick.increase(product)
                }
            }

            binding.imgDecrease.setOnClickListener {
                if (product.quantity_buy != null && product.quantity_buy!! > 1) {
                    product.quantity_buy = product.quantity_buy!! - 1

                    binding.tvProductNumber.text = product.quantity_buy.toString()
                    Log.d("ShoppingCartAdapter", product.quantity_buy.toString())
                    itemProductOfCartClick.decrease(product)
                }
            }

            binding.cvDeleteProduct.setOnClickListener {
                itemProductOfCartClick.delete(product)
            }
        }
    }
}