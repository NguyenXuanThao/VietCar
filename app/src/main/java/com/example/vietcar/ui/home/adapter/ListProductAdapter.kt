package com.example.vietcar.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.data.model.product_group.ProductGroup
import com.example.vietcar.databinding.ItemListProductBinding

class ListProductAdapter : RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder>() {

    private var binding: ItemListProductBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<ProductGroup>() {
        override fun areItemsTheSame(oldItem: ProductGroup, newItem: ProductGroup): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ProductGroup, newItem: ProductGroup): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListProductViewHolder {
        binding =
            ItemListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListProductViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ListProductViewHolder, position: Int) {
        val currentList = differ.currentList
        holder.bind(currentList[position])

    }

    inner class ListProductViewHolder(private val binding: ItemListProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productGroup: ProductGroup) {
            val productAdapter = ProductHomeAdapter()
            val productList = productGroup.product.take(4)
            productAdapter.differ.submitList(productList)
            binding.tvNameListProduct.text = productGroup.name
            binding.rvListProduct.adapter = productAdapter
            binding.rvListProduct.layoutManager =
                GridLayoutManager(itemView.context, 2)
        }
    }
}