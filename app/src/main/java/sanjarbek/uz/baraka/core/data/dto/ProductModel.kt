package sanjarbek.uz.baraka.core.data.dto
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class ProductModel(
    val userToken: String? = null,
    val adminToken: String? = null,
    val date: Long? = null,
    val score: Double? = null,
    val status: String? = null,
    val recipient: String? = null,
    val description: String? = null,
) : Parcelable
