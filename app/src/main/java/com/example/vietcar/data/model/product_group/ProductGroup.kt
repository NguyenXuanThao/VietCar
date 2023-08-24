package com.example.vietcar.data.model.product_group

import com.example.vietcar.data.model.product.Product

data class ProductGroup(
    val description: String?,
    val id: Int?,
    val name: String?,
    val product: List<Product>,
    val status: Int?
)