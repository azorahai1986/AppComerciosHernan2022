package com.example.navdrawer.modelos_de_datos

import java.io.Serializable

class ModeloPdf(var producto:String, var cantidad:Int, var precio:Double, var subTotal:Double):
    Serializable {
    constructor() : this("", 0, 0.0, 0.0)
}