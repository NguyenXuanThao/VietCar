package com.example.vietcar.ui.city.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.click.ItemCityClick
import com.example.vietcar.data.model.location.city.City
import com.example.vietcar.databinding.ItemLocationLayoutBinding

class CityAdapter(private val itemCityClick: ItemCityClick) :
    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private var binding: ItemLocationLayoutBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        binding =
            ItemLocationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val currentList = differ.currentList
        holder.bind(currentList[position])

    }

    inner class CityViewHolder(private val binding: ItemLocationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(city: City) {
            binding.tvName.text = city.name

            itemView.setOnClickListener {
                itemCityClick.onClickCityItem(city)
            }
        }
    }

}