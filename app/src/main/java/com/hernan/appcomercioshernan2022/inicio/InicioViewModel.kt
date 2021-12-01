package com.hernan.appcomercioshernan2022.inicio

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InicioViewModel : ViewModel() {

    lateinit var home:HomeFragment
    fun obtenerUser():String{
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email.toString()
            val photoUrl = user.photoUrl

            val emailVerified = user.isEmailVerified
            home = HomeFragment()
            val uid = user.uid

        }
        return user?.email.toString()
    }
    private val email = MutableLiveData<String>().apply {
        value = obtenerUser()

    }
    val mail: LiveData<String> = email
}