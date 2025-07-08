package sanjarbek.uz.baraka.core.event

import sanjarbek.uz.baraka.core.data.dto.ProductModel
import sanjarbek.uz.baraka.core.data.dto.UserModel

sealed class SignInEvent {
    data class Success(val message: String, val token: String) : SignInEvent()
    data class Error(val message: String) : SignInEvent()
    data object Loading : SignInEvent()
}

sealed class GetUsersEvent {
    data class Success(val user:UserModel) : GetUsersEvent()
    data class Error(val message: String) : GetUsersEvent()
    data object Loading : GetUsersEvent()
}

sealed class GetProductsEvent {
    data class Success(val products: List<ProductModel>) : GetProductsEvent()
    data class Error(val message: String) : GetProductsEvent()
    data object Loading : GetProductsEvent()
}