package com.example.vietcar.data.model.bill

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val address: String?,
    val city_id: Int?,
    val city_name: String?,
    val code: String?,
    val created_at: String?,
    val cumulative_poInts: Int?,
    val customer_id: Int?,
    val customer_name: String?,
    val customer_phone: String?,
    val debit_price: Int?,
    val delivery_address_id: Int?,
    val delivery_method: Int?,
    val discount_voucher: Int?,
    val district_id: Int?,
    val district_name: String?,
    val fee_ship: Int?,
    val id: Int?,
    val net_price: Int?,
    val note: String?,
    val number_product_of_bill: Int?,
    val payment_method: Int?,
    val payment_price: Int?,
    val payment_status: Int?,
    val price_product: Int?,
    val step: Int?,
    val updated_at: String?,
    val ward_id: Int?,
    val ward_name: String?
): Parcelable