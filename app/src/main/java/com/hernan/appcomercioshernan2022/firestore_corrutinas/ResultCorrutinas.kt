package com.hernan.appcomercioshernan2022.firestore_corrutinas

sealed class Result<out R> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val error: Throwable): Result<Nothing>()
}