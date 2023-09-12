package com.example.vietcar.di.repository

import com.example.vietcar.common.DataLocal
import com.example.vietcar.data.api.CarApi
import com.example.vietcar.data.api.LocationApi
import com.example.vietcar.data.model.account.AccountInformation
import com.example.vietcar.data.model.address.AddressBody
import com.example.vietcar.data.model.address.AddressResult
import com.example.vietcar.data.model.address.ListAddress
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
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product.ProductBody
import com.example.vietcar.data.model.product.ProductOfCartBody
import com.example.vietcar.data.model.product_group.ListProductGroup
import com.example.vietcar.data.model.product_to_cart.ProductToCart
import com.example.vietcar.data.model.register.RegisterBody
import com.example.vietcar.data.model.register.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val carApi: CarApi,
    private val locationApi: LocationApi
) : ICarRepository {

    override suspend fun getCategory(): ListCategory {
        return withContext(Dispatchers.IO) {
            carApi.getCategory(DataLocal.BEARER_TOKEN)
        }
    }

    override suspend fun getProductGroup(): ListProductGroup {
        return withContext(Dispatchers.IO) {
            carApi.getProductGroup(DataLocal.BEARER_TOKEN)
        }
    }

    override suspend fun getListProductCategory(categoryId: String): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getListProductCategory(DataLocal.BEARER_TOKEN, categoryId = categoryId)
        }
    }

    override suspend fun getListProductGroup(groupId: String): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getListProductGroup(DataLocal.BEARER_TOKEN, groupId = groupId)
        }
    }

    override suspend fun getAllProduct(): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getAllProduct(DataLocal.BEARER_TOKEN)
        }
    }

    override suspend fun login(loginBody: LoginBody): LoginResponse {
        return withContext(Dispatchers.IO) {
            carApi.login(token = DataLocal.BEARER_TOKEN, body = loginBody)
        }
    }

    override suspend fun register(registerBody: RegisterBody): RegisterResponse {
        return withContext(Dispatchers.IO) {
            carApi.register(token = DataLocal.BEARER_TOKEN, body = registerBody)
        }
    }

    override suspend fun getAccountInformation(): AccountInformation {
        return withContext(Dispatchers.IO) {
            carApi.getAccountInformation(token = DataLocal.BEARER_TOKEN)
        }
    }

    override suspend fun getRelatedProducts(productId: String): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getRelatedProducts(token = DataLocal.BEARER_TOKEN, productId = productId)
        }
    }

    override suspend fun getProductShoppingCart(): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getProductShoppingCart(DataLocal.BEARER_TOKEN)
        }
    }

    override suspend fun createDraftBill(body: BillBody): BillResponse {
        return withContext(Dispatchers.IO) {
            carApi.createDraftBill(token = DataLocal.BEARER_TOKEN, body = body)
        }
    }

    override suspend fun getListBill(): ListBill {
        return withContext(Dispatchers.IO) {
            carApi.getListBill(token = DataLocal.BEARER_TOKEN)
        }
    }

    override suspend fun getBillDetail(id: Int): BillDetail {
        return withContext(Dispatchers.IO) {
            carApi.getBillDetail(token = DataLocal.BEARER_TOKEN, id = id)
        }
    }

    override suspend fun orderProduct(body: OrderBody): BillDetail {
        return withContext(Dispatchers.IO) {
            carApi.orderProduct(token = DataLocal.BEARER_TOKEN, body = body)
        }
    }

    override suspend fun addProductToCart(body: ProductBody): ProductToCart {
        return withContext(Dispatchers.IO) {
            carApi.addProductToCart(DataLocal.BEARER_TOKEN, body = body)
        }
    }

    override suspend fun updateQuantity(body: ProductOfCartBody): ProductToCart {
        return withContext(Dispatchers.IO) {
            carApi.updateQuantity(DataLocal.BEARER_TOKEN, body = body)
        }
    }

    override suspend fun deleteProductOfCart(cartId: Int): ProductToCart {
        return withContext(Dispatchers.IO) {
            carApi.deleteProductOfCart(DataLocal.BEARER_TOKEN, cartId = cartId)
        }
    }

    override suspend fun getAddress(): ListAddress {
        return withContext(Dispatchers.IO) {
            carApi.getAddress(DataLocal.BEARER_TOKEN)
        }
    }

    override suspend fun addAddress(body: AddressBody): AddressResult {
        return withContext(Dispatchers.IO) {
            carApi.addAddress(DataLocal.BEARER_TOKEN, body = body)
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

}