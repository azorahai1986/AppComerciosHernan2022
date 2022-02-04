package com.hernan.appcomercioshernan2022.clases_push.clases_push

import android.net.Uri

data class NotificationData(
    val title: String,
    val message: String,
    val idProd: String = "",
    val arrayURLs: String
) {

}