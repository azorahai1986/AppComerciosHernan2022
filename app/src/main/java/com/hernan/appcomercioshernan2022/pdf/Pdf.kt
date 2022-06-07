package com.hernan.appcomercioshernan2022.pdf

data class Pdf(var id: String = "",
               var nDeOrden: String = "",
               val nombreProducto: String = "",
               var cantidades: String = "",
               val imagen: String = "",
               var precio: String = "",
               var subtotal: String = "") {
}