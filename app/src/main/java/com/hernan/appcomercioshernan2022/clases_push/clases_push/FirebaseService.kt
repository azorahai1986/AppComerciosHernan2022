package com.hernan.appcomercioshernan2022.clases_push.clases_push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hernan.appcomercioshernan2022.actividades.MainActivity
import kotlin.random.Random


private const val CHANNEL_ID = "my_channel"
class FirebaseService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        var bigBitmapImage:Bitmap? = null
        //val big_bitmap_image = BitmapFactory.decodeResource(resources, com.hernan.appcomercioshernan2022.R.drawable.camara)
        val imagenUrl = message.data["arrayURLs"]
        Log.e("Imagen recibida URL", imagenUrl.toString())

        Glide.with(this)
            .asBitmap()
            .load(imagenUrl)
            .into(object : CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    bigBitmapImage = resource
                    notificate(resource, message)
                }
            })



    }
    private fun notificate(resource: Bitmap, message: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }


        intent.putExtra("idProd", message.data["idProd"])
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)



        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
            .setSmallIcon(com.hernan.appcomercioshernan2022.R.drawable.logo_cs_transparente ).setAutoCancel(true)
            //.setLargeIcon(BitmapFactory.decodeResource(resources, com.hernan.appcomercioshernan2022.R.drawable.cds_logo))
            .setContentIntent(pendingIntent).build()


        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }

        notificationManager.createNotificationChannel(channel)

    }
}