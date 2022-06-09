package com.hernan.appcomercioshernan2022.enlace_con_firebase.viewmodels_crud


import androidx.lifecycle.ViewModel
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria


class ViewModelFirestore:ViewModel() {
    var dataFirestore = ModeloDeIndumentaria()
    var dataListaCarrito = mutableListOf<ModeloDeIndumentaria>()
    var position = 0




}
