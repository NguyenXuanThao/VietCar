package com.example.vietcar.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vietcar.common.DataLocal
import com.example.vietcar.data.model.category.Category
import com.example.vietcar.databinding.ItemCategoryBinding
import com.example.vietcar.ui.home.fragment.HomeFragmentDirections

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ItemCategoryViewHolder>() {

    private var binding: ItemCategoryBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCategoryViewHolder {
        binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemCategoryViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemCategoryViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ItemCategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Category) {
            binding.tvNameCategory.text = data.name
            val uriImage = "https://vietcargroup.com${data.avatar}"
            val uriScreen = "https://vietcargroup.com/avatar/1669603516.png"

            if (data.avatar != DataLocal.EMPTY) {
                Glide.with(itemView.context).load(uriImage)
                    .into(binding.imgCategory)
            } else {
                Glide.with(itemView.context).load(uriScreen)
                    .into(binding.imgCategory)
            }

            itemView.setOnClickListener { mView ->
                val action = HomeFragmentDirections.actionBottomNavHomeToCategoryFragment(data)
                mView.findNavController().navigate(action)
            }

        }
    }
}