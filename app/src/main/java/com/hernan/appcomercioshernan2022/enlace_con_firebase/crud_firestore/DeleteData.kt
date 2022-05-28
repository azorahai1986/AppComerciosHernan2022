package com.hernan.appcomercioshernan2022.enlace_con_firebase.crud_firestore

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.airbnb.lottie.animation.content.Content
import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.appcomercioshernan2022.actividades.MainActivity
import com.hernan.appcomercioshernan2022.enlace_con_firebase.firestoreData
import com.hernan.appcomercioshernan2022.inicio.HomeFragment
import com.hernan.appcomercioshernan2022.verImagen.VerImagenFragment

class DeleteData() {

    val fragment = AppCompatActivity()
    val mifragment = VerImagenFragment()
    val home = HomeFragment()
    fun deleteFirestore(recibirId:String, context: Context?){
        firestoreData().collection("ModeloDeIndumentaria")
            .document(recibirId.toString())
            .delete().addOnSuccessListener {
                Toast.makeText(context, "Archivo Eliminado", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(context, "Falló", Toast.LENGTH_SHORT).show()

            }


    }
}