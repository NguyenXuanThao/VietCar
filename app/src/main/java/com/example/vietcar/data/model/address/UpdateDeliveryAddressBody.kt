package com.example.vietcar.data.model.address

data class UpdateDeliveryAddressBody(
    val address: Int?,
    val city_id: Int?,
    val customer_name: String?,
    val customer_phone: String?,
    val delivery_id: Int?,
    val district_id: Int?,
    val is_default: Int?,
    val ward_id: Int?
)