package com.example.vietcar.ui.product_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vietcar.R
import com.example.vietcar.click.ItemShoppingCartClick
import com.example.vietcar.data.model.product.Product
import com.example.vietcar.databinding.ItemProductRelatedBinding
import com.example.vietcar.ui.product_detail.fragment.ProductDetailFragmentDirections

class ProductDetailAdapter(private val itemShoppingCartClick: ItemShoppingCartClick) :
    RecyclerView.Adapter<ProductDetailAdapter.ProductDetailViewHolder>() {

    private var binding: ItemProductRelatedBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailViewHolder {
        binding =
            ItemProductRelatedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductDetailViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductDetailViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ProductDetailViewHolder(private val binding: ItemProductRelatedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvNameProduct.text = product.name
            val price = product.total_price?.let { String.format("%,d đ", it.toLong()) }
            binding.tvPriceProduct.text = price

            val uriImage = "https://vietcargroup.com${product.avatar}"
            Glide.with(itemView.context).load(uriImage)
                .placeholder(R.drawable.img_default)
                .into(binding.imgProduct)

            binding.imgShopping.setOnClickListener {
                itemShoppingCartClick.onClickShoppingCartItem(product)
            }

            itemView.setOnClickListener { mView ->
                val action = ProductDetailFragmentDirections.actionDetailProductFragmentSelf(
                    product
                )

                mView.findNavController().navigate(action)
            }
        }
    }
}