package com.example.vietcar.data.model.product_group

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ListProductGroup(
    val `data`: List<ProductGroup>,
    val message: String?,
    val status: Int?
) : Parcelable