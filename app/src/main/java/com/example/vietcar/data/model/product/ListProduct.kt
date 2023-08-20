package com.example.vietcar.data.model.product

data class ListProduct(
    val `data`: List<ProductData>,
    val message: String,
    val status: Int
)