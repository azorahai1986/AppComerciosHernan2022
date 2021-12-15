package com.hernan.appcomercioshernan2022.firestore_corrutinas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewModelCorrutinas(private val modelRepository: RepoCorrutinas = RepoCorrutinas()): ViewModel() {
    val firestoreData = MutableLiveData<ModeloDeIndumentaria>()
    val error = MutableLiveData<Throwable>()
    //val repo = RepoCorrutinas()
    fun getFirestore() {
        viewModelScope.launch {
            modelRepository.getFirestoreData().collect {

                when(it){
                    is Result.Success -> firestoreData.value = (it.data as ModeloDeIndumentaria?)!!
                    is Result.Error -> error.value = it.error
                }
            }
        }
    }

    fun removeListener() {
        modelRepository.removeListener()
    }


}