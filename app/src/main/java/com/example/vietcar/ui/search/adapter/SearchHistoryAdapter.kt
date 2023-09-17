package com.example.vietcar.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.click.DeleteSearchHistory
import com.example.vietcar.data.model.search_history.SearchHistoryEntity
import com.example.vietcar.databinding.ItemSearchHistoryBinding
import com.example.vietcar.ui.search.fragment.SearchFragmentDirections

class SearchHistoryAdapter(private val deleteSearchHistory: DeleteSearchHistory) :
    RecyclerView.Adapter<SearchHistoryAdapter.SearchViewHolder>() {

    private var binding: ItemSearchHistoryBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<SearchHistoryEntity>() {
        override fun areItemsTheSame(
            oldItem: SearchHistoryEntity,
            newItem: SearchHistoryEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SearchHistoryEntity,
            newItem: SearchHistoryEntity
        ): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        binding =
            ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class SearchViewHolder(private val binding: ItemSearchHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(searchHistoryEntity: SearchHistoryEntity) {
            binding.tvName.text = searchHistoryEntity.query

            binding.cvDeleteProduct.setOnClickListener {
                deleteSearchHistory.deleteSearchHistoryClick(searchHistoryEntity)
            }

            itemView.setOnClickListener { mView ->
                val action = SearchFragmentDirections.actionSearchFragmentToDetailSearchFragment(
                    searchHistoryEntity.query
                )
                mView.findNavController().navigate(action)
            }
        }
    }
}