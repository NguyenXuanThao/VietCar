package com.example.vietcar.di.repository

import com.example.vietcar.data.api.CarApi
import com.example.vietcar.data.api.LocationApi
import com.example.vietcar.data.db.CarDao
import com.example.vietcar.data.model.account.AccountInformation
import com.example.vietcar.data.model.address.AddressBody
import com.example.vietcar.data.model.address.AddressResult
import com.example.vietcar.data.model.address.ListAddress
import com.example.vietcar.data.model.address.UpdateDeliveryAddressBody
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val carApi: CarApi,
    private val locationApi: LocationApi,
    private val carDao: CarDao
) : ICarRepository {
    override suspend fun getBanner(): ListBanner {
        return withContext(Dispatchers.IO) {
            carApi.getBanner()
        }
    }

    override suspend fun getCategory(): ListCategory {
        return withContext(Dispatchers.IO) {
            carApi.getCategory()
        }
    }

    override suspend fun getProductGroup(): ListProductGroup {
        return withContext(Dispatchers.IO) {
            carApi.getProductGroup()
        }
    }

    override suspend fun getListProductCategory(categoryId: String): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getListProductCategory(categoryId = categoryId)
        }
    }

    override suspend fun getListProductGroup(groupId: String): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getListProductGroup(groupId = groupId)
        }
    }

    override suspend fun getAllProduct(): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getAllProduct()
        }
    }

    override suspend fun getAllProductSearch(keySearch: String): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getAllProduct(keySearch = keySearch)
        }
    }

    override suspend fun login(loginBody: LoginBody): LoginResponse {
        return withContext(Dispatchers.IO) {
            carApi.login(body = loginBody)
        }
    }

    override suspend fun register(registerBody: RegisterBody): RegisterResponse {
        return withContext(Dispatchers.IO) {
            carApi.register(body = registerBody)
        }
    }

    override suspend fun getAccountInformation(): AccountInformation {
        return withContext(Dispatchers.IO) {
            carApi.getAccountInformation()
        }
    }

    override suspend fun getRelatedProducts(productId: String): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getRelatedProducts(productId = productId)
        }
    }

    override suspend fun getProductShoppingCart(): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getProductShoppingCart()
        }
    }

    override suspend fun createDraftBill(body: BillBody): BillResponse {
        return withContext(Dispatchers.IO) {
            carApi.createDraftBill(body = body)
        }
    }

    override suspend fun getListBill(): ListBill {
        return withContext(Dispatchers.IO) {
            carApi.getListBill()
        }
    }

    override suspend fun getBillDetail(id: Int): BillDetail {
        return withContext(Dispatchers.IO) {
            carApi.getBillDetail(id = id)
        }
    }

    override suspend fun orderProduct(body: OrderBody): BillDetail {
        return withContext(Dispatchers.IO) {
            carApi.orderProduct(body = body)
        }
    }

    override suspend fun addProductToCart(body: ProductBody): ProductToCart {
        return withContext(Dispatchers.IO) {
            carApi.addProductToCart(body = body)
        }
    }

    override suspend fun updateQuantity(body: ProductOfCartBody): ProductToCart {
        return withContext(Dispatchers.IO) {
            carApi.updateQuantity(body = body)
        }
    }

    override suspend fun deleteProductOfCart(cartId: Int): ProductToCart {
        return withContext(Dispatchers.IO) {
            carApi.deleteProductOfCart(cartId = cartId)
        }
    }

    override suspend fun getAddress(): ListAddress {
        return withContext(Dispatchers.IO) {
            carApi.getAddress()
        }
    }

    override suspend fun addAddress(body: AddressBody): AddressResult {
        return withContext(Dispatchers.IO) {
            carApi.addAddress(body = body)
        }
    }

    override suspend fun deleteAddress(deliveryId: Int): AddressResult {
        return withContext(Dispatchers.IO) {
            carApi.deleteAddress(deliveryId = deliveryId)
        }
    }

    override suspend fun updateAddress(body: UpdateDeliveryAddressBody): AddressResult {
        return withContext(Dispatchers.IO) {
            carApi.updateAddress(body = body)
        }
    }

    override suspend fun getCity(): ListCity {
        return withContext(Dispatchers.IO) {
            locationApi.getCity()
        }
    }

    override suspend fun getDistrict(provinceCode: String): ListDistrict {
        return withContext(Dispatchers.IO) {
            locationApi.getDistrict(provinceCode)
        }
    }

    override suspend fun getWards(districtCode: String): ListWards {
        return withContext(Dispatchers.IO) {
            locationApi.getWards(districtCode)
        }
    }

    override suspend fun changePassword(body: PasswordBody): AccountInformation {
        return withContext(Dispatchers.IO) {
            carApi.changePassword(body = body)
        }
    }

    override suspend fun insertText(searchHistoryEntity: SearchHistoryEntity) {
        return withContext(Dispatchers.IO) {
            carDao.insertText(searchHistoryEntity)
        }
    }

    override fun getAllText(): Flow<List<SearchHistoryEntity>> {
        return carDao.getText().flowOn(Dispatchers.IO)
    }

    override suspend fun deleteText(searchHistoryEntity: SearchHistoryEntity) {
        carDao.deleteText(searchHistoryEntity)
    }

}