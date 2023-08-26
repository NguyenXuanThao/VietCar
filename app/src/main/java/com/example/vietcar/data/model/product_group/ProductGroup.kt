package com.example.vietcar.data.model.product_group

import android.os.Parcelable
import com.example.vietcar.data.model.product.Product
import kotlinx.parcelize.Parcelize


@Parcelize
data class ProductGroup(
    val description: String?,
    val id: Int?,
    val name: String?,
    val product: List<Product>,
    val status: Int?
): Parcelable