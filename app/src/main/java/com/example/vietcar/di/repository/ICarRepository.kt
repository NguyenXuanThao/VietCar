package com.example.vietcar.di.repository

import com.example.vietcar.data.model.category.ListCategory
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product_group.ListProductGroup
import com.example.vietcar.data.model.register.RegisterBody
import com.example.vietcar.data.model.register.RegisterResponse

interface ICarRepository {

    suspend fun getCategory(): ListCategory

    suspend fun getProductGroup(): ListProductGroup

    suspend fun getListProductCategory(categoryId: String): ListProduct

    suspend fun getListProductGroup(groupId: String): ListProduct
    suspend fun getAllProduct(): ListProduct

    suspend fun login(loginBody: LoginBody): LoginResponse

    suspend fun register(registerBody: RegisterBody): RegisterResponse

    suspend fun getRelatedProducts(productId: String): ListProduct
}