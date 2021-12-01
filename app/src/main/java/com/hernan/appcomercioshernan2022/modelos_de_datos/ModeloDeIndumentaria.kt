package com.hernan.appcomercioshernan2022.modelos_de_datos

class ModeloDeIndumentaria(
    var id: String = "",
    val cate: String = "",
    val marca: String = "",
    val imagen: String = "",
    val arrayImagen: ArrayList<String> = arrayListOf(),
    val nombre: String = "",
    var precio: String = ""
){
}