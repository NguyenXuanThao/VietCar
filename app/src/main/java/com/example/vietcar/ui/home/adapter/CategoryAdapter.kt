package com.example.vietcar.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vietcar.click.ItemCategoryClick
import com.example.vietcar.common.DataLocal
import com.example.vietcar.data.model.category.Category
import com.example.vietcar.databinding.ItemCategoryBinding

class CategoryAdapter(private val itemCategoryClick: ItemCategoryClick) :
    RecyclerView.Adapter<CategoryAdapter.ItemCategoryViewHolder>() {

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

        fun bind(category: Category) {
            binding.tvNameCategory.text = category.name
            val uriImage = "https://vietcargroup.com${category.avatar}"
            val uriScreen = "https://vietcargroup.com/avatar/1669603516.png"

            if (category.avatar != DataLocal.EMPTY) {
                Glide.with(itemView.context).load(uriImage)
                    .into(binding.imgCategory)
            } else {
                Glide.with(itemView.context).load(uriScreen)
                    .into(binding.imgCategory)
            }

            itemView.setOnClickListener {
                itemCategoryClick.onClickCategoryItem(adapterPosition)
            }
        }
    }
}