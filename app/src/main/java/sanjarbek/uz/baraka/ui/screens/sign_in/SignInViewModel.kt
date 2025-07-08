package sanjarbek.uz.baraka.ui.screens.sign_in

import sanjarbek.uz.baraka.core.event.SignInEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanjarbek.uz.baraka.MyApplication

class SignInViewModel : ViewModel() {

    private val _signInEvent = MutableLiveData<SignInEvent>()
    val signInEvent: LiveData<SignInEvent> get() = _signInEvent

    fun signIn(email: String, password: String) {
        _signInEvent.value = SignInEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _signInEvent.value = SignInEvent.Success(
                            "Muvaffaqqiyatli hisobga kirildi",
                            it.result.user?.uid!!
                        )
                    } else {
                        _signInEvent.postValue(
                            SignInEvent.Error(
                                it.exception?.message.toString()
                            )
                        )
                    }
                }
                .addOnFailureListener {
                    _signInEvent.value = SignInEvent.Error(it.message.toString())
                }
        }
    }
}