package com.hernan.appcomercioshernan2022.user_data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


fun userData(): FirebaseUser? = FirebaseAuth.getInstance().currentUser