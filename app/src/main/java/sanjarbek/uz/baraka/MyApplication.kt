package sanjarbek.uz.baraka

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import sanjarbek.uz.baraka.core.constants.Constants

class MyApplication : Application() {

    companion object {
        lateinit var sharedPrefs: SharedPreferences
        lateinit var dbReference: DatabaseReference
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        dbReference =
            FirebaseDatabase.getInstance("https://baraka-1bb8f-default-rtdb.firebaseio.com/").reference
        sharedPrefs = getSharedPreferences(Constants.BARAKA_ADMIN, Context.MODE_PRIVATE)
    }
}