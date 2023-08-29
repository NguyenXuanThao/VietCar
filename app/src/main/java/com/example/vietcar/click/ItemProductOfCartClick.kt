package com.example.vietcar.click

import com.example.vietcar.data.model.product.Product

interface ItemProductOfCartClick {

    fun increase(product: Product)
    fun decrease(product: Product)

    fun delete(product: Product)
}