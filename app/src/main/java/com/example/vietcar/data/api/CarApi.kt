package com.example.vietcar.data.api

import com.example.vietcar.common.DataLocal
import com.example.vietcar.data.model.address.ListAddress
import com.example.vietcar.data.model.category.ListCategory
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.data.model.product.ProductOfCartBody
import com.example.vietcar.data.model.product_group.ListProductGroup
import com.example.vietcar.data.model.product_to_cart.ProductToCart
import com.example.vietcar.data.model.register.RegisterBody
import com.example.vietcar.data.model.register.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface CarApi {

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

    @GET("api/noauth/getListCategory")
    suspend fun getCategory(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("pageSize") pageSize: String = "10"
    ): ListCategory


    @GET("api/noauth/getListGroup")
    suspend fun getProductGroup(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("pageSize") pageSize: String = "10"
    ): ListProductGroup

    @GET("api/noauth/getListProduct")
    suspend fun getListProductCategory(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("category_id") categoryId: String?,
    ): ListProduct

    @GET("api/noauth/getListProduct")
    suspend fun getListProductGroup(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("group_id") groupId: String?,
    ): ListProduct

    @GET("api/noauth/getListProduct")
    suspend fun getAllProduct(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
    ): ListProduct

    @GET("api/noauth/getListProductRelation")
    suspend fun getRelatedProducts(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("product_id") productId: String
    ): ListProduct

    @GET("api/auth/cart/listProduct")
    suspend fun getProductShoppingCart(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
    ): ListProduct

    @POST("api/auth/cart/addProductToCart")
    suspend fun addProductToCart(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: ProductBody
    ): ProductToCart

    @PUT("api/auth/cart/changeQuantityProductOfCart")
    suspend fun updateQuantity(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: ProductOfCartBody
    ): ProductToCart

    @DELETE("api/auth/cart/deleteProductOfCart")
    suspend fun deleteProductOfCart(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("cart_id") cartId: Int
    ): ProductToCart

    @GET("api/auth/bill/listDeliveryAddress")
    suspend fun getAddress(
        @Header("Authorization") token: String,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV
    ): ListAddress


}