package com.hernan.appcomercioshernan2022.pdf

import android.util.Log

class Calculo() {
    fun multiplicar(cantidad:Int, precio:Double):String{

        val calculo = {x:Int, y:Double -> x*y}
        val result = calculo(cantidad, precio)

        return result.toString()

    }


}