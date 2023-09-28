package com.example.vietcar.data.api

import com.example.vietcar.common.DataLocal
import com.example.vietcar.data.model.account.AccountBody
import com.example.vietcar.data.model.account.AccountInformation
import com.example.vietcar.data.model.address.AddressBody
import com.example.vietcar.data.model.address.AddressResult
import com.example.vietcar.data.model.address.ListAddress
import com.example.vietcar.data.model.address.UpdateDeliveryAddressBody
import com.example.vietcar.data.model.avatar.Avatar
import com.example.vietcar.data.model.avatar.AvatarBody
import com.example.vietcar.data.model.banner.ListBanner
import com.example.vietcar.data.model.bill.BillBody
import com.example.vietcar.data.model.bill.BillResponse
import com.example.vietcar.data.model.bill.ListBill
import com.example.vietcar.data.model.bill_detail.BillDetail
import com.example.vietcar.data.model.bill_detail.OrderBody
import com.example.vietcar.data.model.category.ListCategory
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.data.model.password.PasswordBody
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.data.model.product.ProductOfCartBody
import com.example.vietcar.data.model.product_group.ListProductGroup
import com.example.vietcar.data.model.product_to_cart.ProductToCart
import com.example.vietcar.data.model.register.RegisterBody
import com.example.vietcar.data.model.register.RegisterResponse
import com.example.vietcar.data.model.store.StoreBody
import com.example.vietcar.data.model.store.StoreData
import com.example.vietcar.data.model.store_detail.StoreDetail
import com.example.vietcar.data.model.store_detail.StoreDetailBody
import com.example.vietcar.data.model.store_detail.StoreDetailData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CarApi {

    @GET("api/noauth/listBannerHome")
    suspend fun getBanner(
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV
    ): ListBanner

    @POST("api/noauth/login")
    suspend fun login(
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: LoginBody
    ): LoginResponse

    @POST("api/noauth/register")
    suspend fun register(
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: RegisterBody
    ): RegisterResponse

    @GET("api/auth/user-profile")
    suspend fun getAccountInformation(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN
    ): AccountInformation

    @POST("api/auth/updateinfo")
    suspend fun updateInfo(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Body body: AccountBody
    ): AccountInformation

    @Multipart
    @POST("api/auth/update_avatar")
    suspend fun updateAvatar(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Part image: MultipartBody.Part
    ): Avatar

    @POST("api/noauth/shop_getall")
    suspend fun getAllStore(
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: StoreBody
    ): StoreData

    @POST("api/noauth/detailnews")
    suspend fun getStoreDetail(
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: StoreDetailBody
    ): StoreDetailData

    @GET("api/noauth/getListCategory")
    suspend fun getCategory(
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("pageSize") pageSize: String = "10"
    ): ListCategory


    @GET("api/noauth/getListGroup")
    suspend fun getProductGroup(
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("pageSize") pageSize: String = "10"
    ): ListProductGroup

    @GET("api/noauth/getListProduct")
    suspend fun getListProductCategory(
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("category_id") categoryId: String?,
    ): ListProduct

    @GET("api/noauth/getListProduct")
    suspend fun getListProductGroup(
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("group_id") groupId: String?,
    ): ListProduct

    @GET("api/noauth/getListProduct")
    suspend fun getAllProduct(
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("pageIndex") pageIndex: String = "0",
        @Query("keyword_search") keySearch: String = ""
    ): ListProduct

    @GET("api/noauth/getListProductRelation")
    suspend fun getRelatedProducts(
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("product_id") productId: String
    ): ListProduct

    @GET("api/auth/cart/listProduct")
    suspend fun getProductShoppingCart(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
    ): ListProduct

    @POST("api/auth/cart/addProductToCart")
    suspend fun addProductToCart(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: ProductBody
    ): ProductToCart

    @PUT("api/auth/cart/changeQuantityProductOfCart")
    suspend fun updateQuantity(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: ProductOfCartBody
    ): ProductToCart

    @DELETE("api/auth/cart/deleteProductOfCart")
    suspend fun deleteProductOfCart(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("cart_id") cartId: Int
    ): ProductToCart

    @GET("api/auth/bill/listDeliveryAddress")
    suspend fun getAddress(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV
    ): ListAddress

    @POST("api/auth/bill/createDeliveryAddress")
    suspend fun addAddress(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: AddressBody
    ): AddressResult

    @DELETE("api/auth/bill/removeDeliveryAddress")
    suspend fun deleteAddress(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("delivery_id") deliveryId: Int
    ): AddressResult

    @PUT("api/auth/bill/updateDeliveryAddress")
    suspend fun updateAddress(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: UpdateDeliveryAddressBody
    ): AddressResult

    @POST("api/auth/bill/createDraftBill")
    suspend fun createDraftBill(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: BillBody
    ): BillResponse

    @GET("api/auth/bill/listBillByType")
    suspend fun getListBill(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Query("step") step: Int = 0
    ): ListBill

    @GET("/api/auth/bill/detail/{id}")
    suspend fun getBillDetail(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Path("id") id: Int
    ): BillDetail

    @POST("api/auth/bill/order")
    suspend fun orderProduct(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Header("tokendev") tokeDev: String = DataLocal.TOKEN_DEV,
        @Body body: OrderBody
    ): BillDetail

    @POST("api/auth/changepassword")
    suspend fun changePassword(
        @Header("Authorization") token: String = DataLocal.BEARER_TOKEN,
        @Body body: PasswordBody
    ): AccountInformation

}