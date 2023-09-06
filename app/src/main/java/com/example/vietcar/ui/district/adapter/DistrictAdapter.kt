package com.example.vietcar.ui.district.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.click.ItemDistrictClick
import com.example.vietcar.data.model.location.city.City
import com.example.vietcar.data.model.location.district.District
import com.example.vietcar.databinding.ItemLocationLayoutBinding

class DistrictAdapter(private val itemDistrictClick: ItemDistrictClick) :
    RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder>() {

    private var binding: ItemLocationLayoutBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<District>() {
        override fun areItemsTheSame(oldItem: District, newItem: District): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: District, newItem: District): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictViewHolder {
        binding =
            ItemLocationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DistrictViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
        val currentList = differ.currentList
        holder.bind(currentList[position])

    }

    inner class DistrictViewHolder(private val binding: ItemLocationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(district: District) {
            binding.tvName.text = district.name

            itemView.setOnClickListener {
                itemDistrictClick.onItemDistrictClick(district)
            }
        }
    }
}