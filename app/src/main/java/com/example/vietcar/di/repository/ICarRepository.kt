package com.example.vietcar.di.repository

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
import com.example.vietcar.data.model.location.city.ListCity
import com.example.vietcar.data.model.location.wards.ListWards
import com.example.vietcar.data.model.location.district.ListDistrict
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
import com.example.vietcar.data.model.search_history.SearchHistoryEntity
import com.example.vietcar.data.model.store.StoreBody
import com.example.vietcar.data.model.store.StoreData
import com.example.vietcar.data.model.store_detail.StoreDetail
import com.example.vietcar.data.model.store_detail.StoreDetailBody
import com.example.vietcar.data.model.store_detail.StoreDetailData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ICarRepository {

    suspend fun getBanner(): ListBanner

    suspend fun getCategory(): ListCategory

    suspend fun getProductGroup(): ListProductGroup

    suspend fun getListProductCategory(categoryId: String): ListProduct
    suspend fun getAllStore(body: StoreBody): StoreData
    suspend fun getStoreDetail(body: StoreDetailBody): StoreDetailData

    suspend fun getListProductGroup(groupId: String): ListProduct
    suspend fun getAllProduct(): ListProduct

    suspend fun getAllProductSearch(keySearch: String): ListProduct

    suspend fun login(loginBody: LoginBody): LoginResponse

    suspend fun register(registerBody: RegisterBody): RegisterResponse

    suspend fun getAccountInformation(): AccountInformation

    suspend fun updateInfo(body: AccountBody): AccountInformation

    suspend fun updateAvatar(image: MultipartBody.Part): Avatar

    suspend fun getRelatedProducts(productId: String): ListProduct

    suspend fun getProductShoppingCart(): ListProduct

    suspend fun createDraftBill(body: BillBody): BillResponse

    suspend fun getListBill(): ListBill

    suspend fun getBillDetail(id: Int): BillDetail

    suspend fun orderProduct(body: OrderBody): BillDetail

    suspend fun addProductToCart(body: ProductBody): ProductToCart

    suspend fun updateQuantity(body: ProductOfCartBody): ProductToCart

    suspend fun deleteProductOfCart(cartId: Int): ProductToCart

    suspend fun getAddress(): ListAddress
    suspend fun addAddress(body: AddressBody): AddressResult

    suspend fun deleteAddress(deliveryId: Int): AddressResult

    suspend fun updateAddress(body: UpdateDeliveryAddressBody): AddressResult

    suspend fun getCity(): ListCity
    suspend fun getDistrict(provinceCode: String): ListDistrict
    suspend fun getWards(districtCode: String): ListWards

    suspend fun changePassword(body: PasswordBody): AccountInformation

    //database

    suspend fun insertText(searchHistoryEntity: SearchHistoryEntity)

    fun getAllText(): Flow<List<SearchHistoryEntity>>

    suspend fun deleteText(searchHistoryEntity: SearchHistoryEntity)
}