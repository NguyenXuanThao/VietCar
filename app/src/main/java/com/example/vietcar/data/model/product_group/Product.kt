package com.example.vietcar.data.model.product_group

data class Product(
    val avatar: String,
    val category_id: Int,
    val description: String,
    val guarantee: String,
    val id: Int,
    val name: String,
    val net_price: Int,
    val number_sell: Int,
    val quantity: Int,
    val sort_description: String,
    val total_price: Int,
    val unit: String
)