package com.example.vietcar.data.model.product_to_cart

data class Data(
    val created_at: String,
    val customer_id: Int,
    val id: Int,
    val product_id: Int,
    val quantity: Int,
    val sort: Int,
    val updated_at: String
)