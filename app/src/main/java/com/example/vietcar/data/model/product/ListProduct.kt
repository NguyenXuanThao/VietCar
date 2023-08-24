package com.example.vietcar.data.model.product

data class ListProduct(
    val `data`: List<Product>,
    val message: String?,
    val status: Int?
)