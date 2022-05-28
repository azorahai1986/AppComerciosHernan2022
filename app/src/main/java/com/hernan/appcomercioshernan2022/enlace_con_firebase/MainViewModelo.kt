package com.hernan.appcomercioshernan2022.enlace_con_firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navdrawer.modelos_de_datos.*
import com.hernan.appcomercioshernan2022.modelos_de_datos.Categorias
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria
import com.hernan.appcomercioshernan2022.modelos_de_datos.SubCategorias

class MainViewModelo: ViewModel() {
    val repo = Repo()

    fun fetchUserData(): LiveData<MutableList<ModeloDeIndumentaria>> {
        val mutableData = MutableLiveData<MutableList<ModeloDeIndumentaria>>()

        repo.getUserData().observeForever {
            mutableData.value = it

        }
        return mutableData

    }

    fun fetchUserDataSimilares(recibirMarca: String?): LiveData<MutableList<PagerSimilares>> {
        val mutableData = MutableLiveData<MutableList<PagerSimilares>>()
        repo.getUserDataSimilares(recibirMarca).observeForever {
            mutableData.value = it

        }
        return mutableData

    }
    fun fetchUserDataOfertas():LiveData<MutableList<CartelPrincipal>>{
        val mutableDataOfertas = MutableLiveData<MutableList<CartelPrincipal>>()
        repo.getOfertas().observeForever {
            mutableDataOfertas.value = it
        }
        return mutableDataOfertas
    }
    fun fetchUserDataCategorias():LiveData<MutableList<Categorias>>{
        val mutabledataCategorias = MutableLiveData<MutableList<Categorias>>()
        repo.getUserDataCategorias().observeForever {
            mutabledataCategorias.value = it
        }
        return mutabledataCategorias
    }

    fun fetchUserDataSubcate(categoriaRecibida: String?): LiveData<MutableList<SubCategorias>> {
        val mutableData = MutableLiveData<MutableList<SubCategorias>>()
        repo.getUserDataSubcate(categoriaRecibida).observeForever {
            mutableData.value = it

        }
        return mutableData

    }

    fun fetchUserDataDependiente(marcaRecibida:String?): LiveData<MutableList<Dependiente>> {
        val mutableData = MutableLiveData<MutableList<Dependiente>>()
        repo.getUserDataDependiente(marcaRecibida).observeForever {
            mutableData.value = it

        }
        return mutableData

    }
    fun fetchUserDataPorcentaje():LiveData<MutableList<SubCategorias>>{
        val mutabledataPorcen = MutableLiveData<MutableList<SubCategorias>>()
        repo.getUserDataPorcentaje().observeForever {
            mutabledataPorcen.value = it
        }
        return mutabledataPorcen
    }

    fun fetchUserDataZoom(idRecibido: String?): LiveData<MutableList<ImagenesViewPager>> {
        val mutableData = MutableLiveData<MutableList<ImagenesViewPager>>()

        repo.getUserDataZoom(idRecibido).observeForever {
            mutableData.value = it

        }
        return mutableData

    }
}