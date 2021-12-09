package com.hernan.appcomercioshernan2022.modelos_de_datos

import com.google.firebase.database.Exclude

class ModeloDeIndumentaria(
    var id: String = "",
    val cate: String = "",
    val marca: String = "",
    val imagen: String = "",
    val arrayImagen: ArrayList<String> = arrayListOf(),
    val nombre: String = "",
    var precio: String = "",
    @Exclude var type:TYPE = TYPE.ADD
){
    enum class TYPE {
        ADD, UPDATE, REMOVE
    }
    override fun equals(other: Any?): Boolean {
        return (other as ModeloDeIndumentaria).id == id
    }
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + cate.hashCode()
        result = 31 * result + marca.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + precio.hashCode()

        return result
    }
}