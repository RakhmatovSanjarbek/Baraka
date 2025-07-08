package sanjarbek.uz.baraka.core.data.dto

data class UserModel(
    val userToken: String? = null,
    val userName: String? = null,
    val userSurname: String? = null,
    val userBornDate: Long = 0,
    val userGender: Int = -1,
    val userPhoneNumber: String? = null,
    val userEmail: String? = null,
    val userPassword: String? = null,
    val userAddress: String? = null,
    var userAllScore: Double = 0.0,
    var userProcessScore: Double = 0.0,
    var userInWalletScore: Double = 0.0,
    var userApprovedScore: Double = 0.0,
    var userPaidScore: Double = 0.0
)
