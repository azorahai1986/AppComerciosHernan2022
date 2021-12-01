package com.example.navdrawer.clases

import android.util.Log
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria
import com.google.firebase.firestore.FirebaseFirestore


class ModificarFirebase {


    fun modificarPorcentajes(porcen: String, id:String){
        var map = mutableMapOf<String, Any>()
        map["porcentaje"] = porcen.toString()
        val editar = FirebaseFirestore.getInstance().collection("Subcatergoria")
            .document(id)
        editar.update(map)
            .addOnSuccessListener {
            }.addOnFailureListener {

            }

    }

    
    fun aplicarCambioPrecios(listData: MutableList<ModeloDeIndumentaria>) {

        var id = ""
        var precio = ""
        var position = 0

        var map = mutableMapOf<String, Any>()
       listData.map {
           id = it.id
           precio = it.precio

           map["precio"] = precio
           val editarPrecio = FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
               .document(id)
           editarPrecio.update(map)
               .addOnSuccessListener {
                   Log.e("PRECIO modif succes", precio)


               }.addOnFailureListener {
                   Log.e("PRECIO modif Error", precio)
                   Log.e("id modif precio2", id[position].toString())
                   Log.e("INDEX2", position.toString())

               }


       }




       


    }




}


