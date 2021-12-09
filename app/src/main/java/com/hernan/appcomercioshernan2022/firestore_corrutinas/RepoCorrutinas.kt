package com.hernan.appcomercioshernan2022.firestore_corrutinas

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

class RepoCorrutinas {
    var listener: ListenerRegistration? = null
    suspend fun getFirestoreData(): Flow<Result<ModeloDeIndumentaria>> = channelFlow {
        listener = getFirestore().addSnapshotListener { value, error ->
            if (error != null) {
                launch {
                    send(Result.Error(error))
                }
                Log.e("PruebaListError", error.toString())
                return@addSnapshotListener
            }
            value?.documentChanges?.forEach {

                val doc = it.document.toObject(ModeloDeIndumentaria::class.java)
                doc.id = it.document.id

                when (it.type) {
                    DocumentChange.Type.ADDED -> doc.type = ModeloDeIndumentaria.TYPE.ADD
                    DocumentChange.Type.MODIFIED -> doc.type = ModeloDeIndumentaria.TYPE.UPDATE
                    DocumentChange.Type.REMOVED -> doc.type = ModeloDeIndumentaria.TYPE.REMOVE
                }
                launch {
                    send(Result.Success(doc))
                }
            }
        }
        awaitClose {
            listener?.remove()
        }
    }//.flowOn(Dispatchers.IO)

    fun removeListener() {
        listener?.remove()
    }


}