package com.hernan.appcomercioshernan2022.enlace_con_firebase.crud_firestore

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class EditFirestore {
    fun mapNombre(nombrerecib: String?, recibirId: String?, context: Context?) {
        var map = mutableMapOf<String, Any>()
        map["nombre"] = nombrerecib!!
        editFirestore(map, recibirId,context)


    }
    fun mapPrecio(preciorecib: String?, recibirId: String?, context: Context?) {
        var map = mutableMapOf<String, Any>()
        map["precio"] = preciorecib!!
        editFirestore(map, recibirId,context)


    }

    fun editFirestore(map: MutableMap<String, Any>, recibirId: String?, context: Context?) {



        val editar = FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
            .document(recibirId.toString())
        editar.update(map)
            .addOnSuccessListener {
                Toast.makeText(context, "Producto Modificado con exito", Toast.LENGTH_SHORT) .show()
            }.addOnFailureListener {
                Toast.makeText(context, "Falló Modificación", Toast.LENGTH_SHORT).show()

            }
    }
}