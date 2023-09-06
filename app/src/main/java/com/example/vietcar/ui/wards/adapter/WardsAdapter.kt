package com.example.vietcar.ui.wards.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.click.ItemWardsClick
import com.example.vietcar.data.model.location.city.City
import com.example.vietcar.data.model.location.wards.Wards
import com.example.vietcar.databinding.ItemLocationLayoutBinding

class WardsAdapter(private val itemWardsClick: ItemWardsClick) :
    RecyclerView.Adapter<WardsAdapter.WardsViewHolder>() {

    private var binding: ItemLocationLayoutBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<Wards>() {
        override fun areItemsTheSame(oldItem: Wards, newItem: Wards): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Wards, newItem: Wards): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WardsViewHolder {
        binding =
            ItemLocationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WardsViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: WardsViewHolder, position: Int) {
        val currentList = differ.currentList
        holder.bind(currentList[position])

    }

    inner class WardsViewHolder(private val binding: ItemLocationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(wards: Wards) {
            binding.tvName.text = wards.name

            itemView.setOnClickListener {
                itemWardsClick.onItemWardsClick(wards)
            }
        }
    }
}