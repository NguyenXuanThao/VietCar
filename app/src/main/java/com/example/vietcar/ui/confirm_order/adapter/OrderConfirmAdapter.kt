package com.example.vietcar.ui.confirm_order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vietcar.data.model.bill.Data
import com.example.vietcar.databinding.ItemOrderHistoryBinding
import com.example.vietcar.ui.order_history.fragment.OrderHistoryFragmentDirections

class OrderConfirmAdapter : RecyclerView.Adapter<OrderConfirmAdapter.OrderConfirmViewHolder>() {

    private var binding: ItemOrderHistoryBinding? = null

    private val diffUtil = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }

    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderConfirmViewHolder {
        binding =
            ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderConfirmViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderConfirmViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class OrderConfirmViewHolder(private val binding: ItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bill: Data) {

            binding.tvCode.text = "Mã đơn hàng: ${bill.code}"
            val price = bill.price_product?.let { String.format("%,d đ", it.toLong()) }
            binding.tvPrice.text = price
            binding.tvNumber.text = "${bill.number_product_of_bill} sản phẩm"
            binding.tvDate.text = "Ngày đặt hàng: ${bill.created_at}"

            itemView.setOnClickListener { view ->
                val action =
                    OrderHistoryFragmentDirections.actionOrderHistoryFragmentToBillDetailFragment(
                        bill.id!!
                    )

                view.findNavController().navigate(action)

            }
        }
    }
}