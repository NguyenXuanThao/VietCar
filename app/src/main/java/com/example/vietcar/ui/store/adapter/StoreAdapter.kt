package com.example.vietcar.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vietcar.R
import com.example.vietcar.data.model.store.Store
import com.example.vietcar.databinding.ItemStoreBinding
import com.example.vietcar.ui.store.fragment.StoreFragmentDirections

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    private var binding: ItemStoreBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<Store>() {
        override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        binding =
            ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class StoreViewHolder(private val binding: ItemStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(store: Store) {
            binding.tvNameStore.text = store.title

            val uriImage = "https://vietcargroup.com${store.url_image}"
            Glide.with(itemView.context).load(uriImage)
                .placeholder(R.drawable.img_default)
                .into(binding.imgStore)

            itemView.setOnClickListener { mView ->
                val action = StoreFragmentDirections.actionBottomNavStoreToStoreDetailFragment(store.id!!)
                mView.findNavController().navigate(action)
            }
        }
    }
}