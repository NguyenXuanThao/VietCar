package com.example.vietcar.data.api

import com.example.vietcar.common.DataLocal
import com.example.vietcar.data.model.category.Category
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product_group.ProductGroup
import com.example.vietcar.data.model.register.RegisterBody
import com.example.vietcar.data.model.register.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CarApi {

    @GET("api/noauth/getListCategory")
    suspend fun getCategory(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("pageSize") pageSize: String = "10"
    ): Category


    @GET("api/noauth/getListGroup")
    suspend fun getProductGroup(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("pageSize") pageSize: String = "10"
    ): ProductGroup

    @GET("api/noauth/getListProduct")
    suspend fun getListProduct(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("category_id") categoryId: String = "0",
        @Query("group_id") groupId: String = "0"
    ): ListProduct

    @POST("api/noauth/login")
    suspend fun login(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: LoginBody
    ): LoginResponse

    @POST("api/noauth/register")
    suspend fun register(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: RegisterBody
    ): RegisterResponse


}