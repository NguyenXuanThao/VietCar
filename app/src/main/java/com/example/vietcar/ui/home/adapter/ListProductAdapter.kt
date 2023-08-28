package com.example.vietcar.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.click.ItemShoppingCartClick
import com.example.vietcar.data.model.product_group.ProductGroup
import com.example.vietcar.databinding.ItemListProductBinding
import com.example.vietcar.ui.home.fragment.HomeFragmentDirections
import com.example.vietcar.ui.product.adapter.ProductAdapter

class ListProductAdapter (private val itemShoppingCartClick: ItemShoppingCartClick) : RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder>() {

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
            val productAdapter = ProductAdapter(itemShoppingCartClick)
            val productList = productGroup.product

            Log.d("ThaoNX4", productList.size.toString())

            productAdapter.differ.submitList(productList)
            binding.tvNameListProduct.text = productGroup.name
            binding.rvListProduct.adapter = productAdapter
            binding.rvListProduct.layoutManager =
                GridLayoutManager(itemView.context, 2)

            binding.tvShowAll.setOnClickListener {mView ->

                val action = HomeFragmentDirections.actionBottomNavHomeToProductGroupFragment(productGroup.id.toString(), adapterPosition)
                mView.findNavController().navigate(action)
            }
        }
    }
}