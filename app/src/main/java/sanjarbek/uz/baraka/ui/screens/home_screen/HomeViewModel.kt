package sanjarbek.uz.baraka.ui.screens.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanjarbek.uz.baraka.MyApplication
import sanjarbek.uz.baraka.core.constants.Constants
import sanjarbek.uz.baraka.core.data.dto.ProductModel
import sanjarbek.uz.baraka.core.data.dto.UserModel
import sanjarbek.uz.baraka.core.event.GetProductsEvent
import sanjarbek.uz.baraka.core.event.GetUsersEvent

class HomeViewModel : ViewModel() {

    private val _getUserEvent = MutableLiveData<GetUsersEvent>()
    val getUserEvent: LiveData<GetUsersEvent> get() = _getUserEvent

    private val _getProductEvent = MutableLiveData<GetProductsEvent>()
    val getProductEvent: LiveData<GetProductsEvent> get() = _getProductEvent

    private var productDataList: MutableList<ProductModel> = mutableListOf()

    private var valueListener: ValueEventListener? = null

    fun getUsers() {
        _getUserEvent.value = GetUsersEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val token = MyApplication.sharedPrefs.getString(Constants.TOKEN, "")
            MyApplication.dbReference.child(Constants.USER)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var foundModel: UserModel? = null
                        for (childSnapshot in snapshot.children) {
                            val model = childSnapshot.getValue(UserModel::class.java)
                            if (model?.userToken == token) {
                                foundModel = model
                                break
                            }
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            if (foundModel != null) {
                                _getUserEvent.value = GetUsersEvent.Success(foundModel)
                            } else {
                                _getUserEvent.value = GetUsersEvent.Error("Foydalanuvchi topilmadi")
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        CoroutineScope(Dispatchers.Main).launch {
                            _getUserEvent.value = GetUsersEvent.Error(error.message)
                        }
                    }
                })
        }
    }

    fun getProducts() {
        _getProductEvent.value = GetProductsEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val token = MyApplication.sharedPrefs.getString(Constants.TOKEN, "")
            MyApplication.dbReference.child(Constants.RECEIVED_PRODUCT)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        productDataList.clear()
                        for (childSnapshot in snapshot.children) {
                            val model = childSnapshot.getValue(ProductModel::class.java)
                            if (model?.userToken == token) {
                                if (model != null) {
                                    productDataList.add(model)
                                }
                            }
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            if (productDataList.isNotEmpty()) {
                                _getProductEvent.value = GetProductsEvent.Success(productDataList)
                            } else {
                                _getProductEvent.value =
                                    GetProductsEvent.Error("Ma'lumot topilmadi")
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        CoroutineScope(Dispatchers.Main).launch {
                            _getProductEvent.value = GetProductsEvent.Error(error.message)
                        }
                    }

                })

        }

    }

    fun removeListener() {
        valueListener?.let {
            MyApplication.dbReference.removeEventListener(it)
        }

    }
}