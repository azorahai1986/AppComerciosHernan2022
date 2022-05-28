package com.hernan.appcomercioshernan2022.enlace_con_firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


fun firestoreData(): FirebaseFirestore = FirebaseFirestore.getInstance()