package com.example.vietcar.ui.product.adapter

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
import com.example.vietcar.databinding.ItemProductBinding
import com.example.vietcar.ui.category.fragment.CategoryFragmentDirections
import com.example.vietcar.ui.home.fragment.HomeFragmentDirections
import com.example.vietcar.ui.product_group.fragment.ProductGroupFragmentDirections

class ProductAdapter (private val itemShoppingCartClick: ItemShoppingCartClick) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

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

            binding.imgShopping.setOnClickListener {
                itemShoppingCartClick.onClickShoppingCartItem(product)
            }

            itemView.setOnClickListener { mView ->

                val action1 =
                    CategoryFragmentDirections.actionCategoryFragmentToDetailProductFragment(product)
                val action2 =
                    ProductGroupFragmentDirections.actionProductGroupFragmentToDetailProductFragment(
                        product
                    )
                val action3 =
                    HomeFragmentDirections.actionBottomNavHomeToDetailProductFragment(
                        product
                    )

                when (mView.findNavController().currentDestination?.id) {
                    R.id.categoryFragment -> {
                        mView.findNavController().navigate(action1)
                    }
                    R.id.bottomNavHome -> {
                        mView.findNavController().navigate(action3)
                    }
                    else -> {
                        mView.findNavController().navigate(action2)
                    }
                }
            }
        }
    }
}