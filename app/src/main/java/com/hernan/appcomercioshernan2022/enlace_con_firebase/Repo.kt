package com.hernan.appcomercioshernan2022.enlace_con_firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.navdrawer.modelos_de_datos.*
import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.appcomercioshernan2022.modelos_de_datos.Categorias
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria
import com.hernan.appcomercioshernan2022.modelos_de_datos.SubCategorias

class Repo {

    fun getUserData(): LiveData<MutableList<ModeloDeIndumentaria>> {
        /*
        crearé una variable para enviar el precio a la clase AplicarPorcentajesPrecios
         */

        val mutableData = MutableLiveData<MutableList<ModeloDeIndumentaria>>()
        FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
            .get().addOnSuccessListener {

                val listData = mutableListOf<ModeloDeIndumentaria>()

                for (obtenerFireBase in it.documents){
                    val indument = obtenerFireBase.toObject(ModeloDeIndumentaria::class.java)
                    indument?.id = obtenerFireBase.id
                    if (indument != null)

                        listData.add(indument)
                }
                mutableData.value = listData


            }.addOnFailureListener {
                Log.e("ErrorMODELO", it.toString())
                //Esto lo hice para probar si llega internet a la app.
            }
        return mutableData
    }

    fun getOfertas():LiveData<MutableList<CartelPrincipal>>{
        val mutableDatabaseOfertas = MutableLiveData<MutableList<CartelPrincipal>>()
        FirebaseFirestore.getInstance().collection("NuevoModelo").get()
            .addOnSuccessListener {
                val listDataOfertas = mutableListOf<CartelPrincipal>()

                for (ofertas in it.documents){
                    val o = ofertas.toObject(CartelPrincipal::class.java)
                    o?.id = ofertas.id
                    if (o != null){
                        listDataOfertas.add(o)
                    }
                }
                mutableDatabaseOfertas.value = listDataOfertas
            }.addOnFailureListener { Log.e("ErrorMODELO", it.toString()) }
        return mutableDatabaseOfertas
    }

    fun getUserDataSimilares(recibirMarca: String?): LiveData<MutableList<PagerSimilares>> {
        //Log.e("DescripcionRepo", recibirDescripcion)
        val mutableData = MutableLiveData<MutableList<PagerSimilares>>()
        FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria").whereEqualTo("marca", recibirMarca)
            .get().addOnSuccessListener {

                val listData = mutableListOf<PagerSimilares>()
                //Log.e("Datos del repo1", it.toString())
                //Log.e("Datos del repo2", listData.toString())

                for (obtenerFireBase in it.documents){
                    val indument = obtenerFireBase.toObject(PagerSimilares::class.java)
                    indument?.id = obtenerFireBase.id
                    if (indument != null)
                        listData.add(indument)

                }
                //Log.e("Datos del repo3", listData.toString())

                mutableData.value = listData

            }.addOnFailureListener {
                Log.e("ErrorMODELO", it.toString())
                //Esto lo hice para probar si llega internet a la app.
            }
        return mutableData
    }

    fun getUserDataCategorias(): LiveData<MutableList<Categorias>>{
        val mutableCategorias = MutableLiveData<MutableList<Categorias>>()
        FirebaseFirestore.getInstance().collection("Categoria").get().addOnSuccessListener {
            // variable para el for
            val listDataCate = mutableListOf<Categorias>()

            for (datosCate in it.documents){
                val cat = datosCate.toObject(Categorias::class.java)
                cat?.id = datosCate.id
                if (cat != null)
                    listDataCate.add(cat)
            }
            mutableCategorias.value = listDataCate
            //Log.e("Datos del repo2", listDataCate.toString())
        }.addOnFailureListener {
            Log.e("errorCategorias", it.toString())
        }
        return mutableCategorias

    }

    fun getUserDataSubcate(categoriaRecibida: String?): LiveData<MutableList<SubCategorias>> {

        val mutableData = MutableLiveData<MutableList<SubCategorias>>()
        FirebaseFirestore.getInstance().collection("Subcatergoria").whereEqualTo("cate", categoriaRecibida)
            .get().addOnSuccessListener {
                val listData = mutableListOf<SubCategorias>()
                for (obtenerFireBase in it.documents){
                    val indument = obtenerFireBase.toObject(SubCategorias::class.java)
                    indument?.id = obtenerFireBase.id
                    if (indument != null)

                        listData.add(indument)

                }
                mutableData.value = listData
            }.addOnFailureListener {
                Log.e("ErrorMODELO", it.toString())
                //Esto lo hice para probar si llega internet a la app.
            }
        return mutableData
    }

    fun getUserDataDependiente(idRecibido:String?): LiveData<MutableList<Dependiente>> {

        val mutableData = MutableLiveData<MutableList<Dependiente>>()
        FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria").whereEqualTo("marca", idRecibido)
            .get().addOnSuccessListener {

                val listData = mutableListOf<Dependiente>()

                for (obtenerFireBase in it.documents){
                    val indument = obtenerFireBase.toObject(Dependiente::class.java)
                    indument?.id = obtenerFireBase.id
                    if (indument != null)

                        listData.add(indument)

                }
                mutableData.value = listData

            }.addOnFailureListener {
                Log.e("ErrorMODELO", it.toString())
                //Esto lo hice para probar si llega internet a la app.
            }
        return mutableData
    }
    fun getUserDataPorcentaje(): LiveData<MutableList<SubCategorias>>{
        val mutablePorcen = MutableLiveData<MutableList<SubCategorias>>()
        FirebaseFirestore.getInstance().collection("Subcatergoria").get().addOnSuccessListener {
            // variable para el for
            val listDataCate = mutableListOf<SubCategorias>()

            for (datosPorcen in it.documents){
                val cat = datosPorcen.toObject(SubCategorias::class.java)
                cat?.id = datosPorcen.id
                if (cat != null)
                    listDataCate.add(cat)
            }
            mutablePorcen.value = listDataCate
            //Log.e("Datos del repo2", listDataCate.toString())
        }.addOnFailureListener {
            Log.e("errorCategorias", it.toString())
        }
        return mutablePorcen

    }
    fun getUserDataZoom(idRecibido: String?): LiveData<MutableList<ImagenesViewPager>> {

        val mutableData = MutableLiveData<MutableList<ImagenesViewPager>>()
        FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria").whereEqualTo("id", idRecibido )
            .get().addOnSuccessListener {
                Log.e("ZoomRepo", idRecibido.toString())

                val listData = mutableListOf<ImagenesViewPager>()

                for (obtenerFireBase in it.documents){
                    val indument = obtenerFireBase.toObject(ImagenesViewPager::class.java)
                    indument?.id = obtenerFireBase.id
                    if (indument != null)

                        listData.add(indument)

                }
                mutableData.value = listData

            }.addOnFailureListener {
                Log.e("ErrorMODELO", it.toString())
                //Esto lo hice para probar si llega internet a la app.
            }
        return mutableData
    }

}