package com.example.vietcar.di.repository

import com.example.vietcar.data.model.category.Category
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product_group.ProductGroup
import com.example.vietcar.data.model.register.RegisterBody
import com.example.vietcar.data.model.register.RegisterResponse

interface ICarRepository {

    suspend fun getCategory(): Category

    suspend fun getProductGroup(): ProductGroup

    suspend fun getListProduct(): ListProduct

    suspend fun login(loginBody: LoginBody) : LoginResponse

    suspend fun register(registerBody: RegisterBody): RegisterResponse
}