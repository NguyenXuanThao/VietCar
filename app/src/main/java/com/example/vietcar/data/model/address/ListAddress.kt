package com.example.vietcar.data.model.address

data class ListAddress(
    val `data`: List<Address>,
    val message: String?,
    val status: Int?
)