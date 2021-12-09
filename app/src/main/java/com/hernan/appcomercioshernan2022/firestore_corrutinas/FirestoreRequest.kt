package com.hernan.appcomercioshernan2022.firestore_corrutinas

import com.google.firebase.firestore.FirebaseFirestore


fun getFirestore() = FirebaseFirestore.getInstance()
    .collection("ModeloDeIndumentaria")
