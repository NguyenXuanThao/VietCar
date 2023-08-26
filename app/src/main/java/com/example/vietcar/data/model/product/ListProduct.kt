package com.example.vietcar.data.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListProduct(
    val `data`: List<Product>,
    val message: String?,
    val status: Int?
): Parcelable