package com.example.vietcar.data.model.product_group

data class ProductGroupData(
    val description: String,
    val id: Int,
    val name: String,
    val product: List<Product>,
    val status: Int
)