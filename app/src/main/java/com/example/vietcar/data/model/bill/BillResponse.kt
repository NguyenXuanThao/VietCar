package com.example.vietcar.data.model.bill

data class BillResponse(
    val `data`: Data,
    val message: String?,
    val status: Int?
)