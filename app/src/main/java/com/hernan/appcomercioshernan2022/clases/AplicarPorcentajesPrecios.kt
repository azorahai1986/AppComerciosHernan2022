package com.example.navdrawer.clases

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria
import com.google.firebase.firestore.FirebaseFirestore


class AplicarPorcentajesPrecios {

    var preciosObtenidos:ArrayList<String> = ArrayList()
    var modificarFirebase = ModificarFirebase()

    fun obtenerdata(porcen: String, listData: MutableList<ModeloDeIndumentaria>) {


        var position = 0
       listData.map {
           it.precio = ((it.precio.toDouble()+it.precio.toDouble()*porcen.toDouble()/100.0).toString())

           preciosObtenidos.add(it.precio)


       }


        modificarFirebase.aplicarCambioPrecios(listData)


    }


    fun userData(porcen: String, id: String): LiveData<MutableList<ModeloDeIndumentaria>> {
            /*
            crearé una variable para enviar el precio a la clase AplicarPorcentajesPrecios
             */

            val mutableData = MutableLiveData<MutableList<ModeloDeIndumentaria>>()
            FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria").whereEqualTo("marca", id)
                .get().addOnSuccessListener {

                    val listData = mutableListOf<ModeloDeIndumentaria>()

                    for (obtenerFireBase in it.documents){
                        val indument = obtenerFireBase.toObject(ModeloDeIndumentaria::class.java)
                        indument?.id = obtenerFireBase.id
                        if (indument != null)

                            listData.add(indument)

                    }
                    mutableData.value = listData
                    obtenerdata(porcen, listData)


                }.addOnFailureListener {
                    Log.e("ErrorMODELO", it.toString())
                    //Esto lo hice para probar si llega internet a la app.
                }
            return mutableData
        }

}