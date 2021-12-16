package com.hernan.appcomercioshernan2022.firestore_corrutinas

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

private const val MODELO_INDU = "ModeloDeIndumentaria"
fun getFirestore() = FirebaseFirestore.getInstance()
    .collection(MODELO_INDU)
    .orderBy("timestamp", Query.Direction.DESCENDING)

suspend fun getProductos() = FirebaseFirestore.getInstance()
    .collection(MODELO_INDU)
    .orderBy("timestamp", Query.Direction.DESCENDING)
    .get().await()
