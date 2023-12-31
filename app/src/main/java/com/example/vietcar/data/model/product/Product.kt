package com.example.vietcar.data.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Product(
    val avatar: String?,
    val category_id: Int?,
    val cart_id: Int?,
    val description: String?,
    val flag_price: Int?,
    val id: Int?,
    val name: String?,
    val net_price: Int?,
    val number_sell: Int?,
    val quantity: Int?,
    val sort: Int?,
    val sort_description: String?,
    val status: Int?,
    val total_price: Int?,
    val unit: String?,
    var quantity_buy: Int?
) : Parcelable