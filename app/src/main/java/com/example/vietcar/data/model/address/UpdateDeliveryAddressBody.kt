package com.example.vietcar.data.model.address

data class UpdateDeliveryAddressBody(
    val address: String?,
    val city_id: String?,
    val customer_name: String?,
    val customer_phone: String?,
    val delivery_id: String?,
    val district_id: String?,
    val is_default: String?,
    val ward_id: String?
)