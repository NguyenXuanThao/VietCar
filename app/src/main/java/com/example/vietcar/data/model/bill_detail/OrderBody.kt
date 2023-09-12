package com.example.vietcar.data.model.bill_detail

data class OrderBody(
    val bill_id: Int?,
    val delivery_address_id: Int?,
    val delivery_method: Int?,
    val note: String?,
    val payment_method: Int?
)