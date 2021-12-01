package com.hernan.appcomercioshernan2022.actividades

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.hernan.appcomercioshernan2022.adapters.CategoriasAdapter
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.hernan.appcomercioshernan2022.modelos_de_datos.Categorias
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.clases_push.clases_push.PushNotification
import com.hernan.appcomercioshernan2022.clases_push.clases_push.Retrofitinstance
import com.hernan.appcomercioshernan2022.databinding.ActivityActividadAgregarBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*



const val TOPIC = "/topics/general"
class ActividadAgregar : AppCompatActivity(), View.OnClickListener {
    // para enviar los push.........................................
    val TAG = "ActividadAgregar"
    var tvSwitch: TextView? = null

    private lateinit var binding:ActivityActividadAgregarBinding
    private val PICK_IMAGE_REQUEST = 1234
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    private var adapter: CategoriasAdapter? = null

    // Método para subir imagenes al firebase storage
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.imageViewSub!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    private fun uploadFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Cargando...")
            progressDialog.show()

            // para modificar los datos de una lista usando firestore..........................

            val imageRef = storageReference!!.child("images/" + UUID.randomUUID().toString())

            var uploadTask = imageRef.putFile(filePath!!)
            Log.e("cantidad", uploadTask.toString())

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    // para editar los datos de la lista...........................................

                    var cate = binding.tvCategoria.text.toString()
                    //var marca = tvMarca.text.toString()
                    var imagen = downloadUri.toString()
                   // var nombre = tvNombre.text.toString()
                   // var precio = etNuevoPrecio.text.toString()
                    var map = mutableMapOf<String, Any>()
                    // map["id"] = id
                    map["cate"] = cate
                   // map["marca"] = marca
                    map["imagen"] = imagen
                   // map["nombre"] = nombre
                   // map["precio"] = precio



                    FirebaseFirestore.getInstance().collection("Categoria")
                        .document().set(map)
                    finish()
                } else {
                    // Handle failures
                    // ...
                }
            }

        }


    }


    private fun showFilerChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
    }

    lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActividadAgregarBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_actividad_agregar)

        //tvSwitch = findViewById(R.id.textview_switch)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        storage = Firebase.storage

        // dare init a firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference


        binding.imageViewSub.setOnClickListener(this)
       
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        // dar funcion al swith..........................................

        binding.btCargar.setOnClickListener { uploadFile() }

        exTraerDatos()

    }

    fun exTraerDatos() {
        viewModel.fetchUserDataCategorias().observe(this, androidx.lifecycle.Observer {
            adapter?.arrayCategorias = it as ArrayList<Categorias>
            adapter?.notifyDataSetChanged()
            val autocompletar = mutableListOf<String>()
            val autocompletarMarca = mutableListOf<String>()
            val autocompletarCate = mutableListOf<String>()

            for (x in it) {
                //autocompletar.add(x.nombre)
                //autocompletarMarca.add(x.marca)
                autocompletarCate.add(x.cate)

            }
            /*val adapterAuto = ArrayAdapter(this, android.R.layout.simple_list_item_1, autocompletar)
            tvNombre.threshold = 0
            tvNombre.setAdapter(adapterAuto)
            tvNombre.setOnFocusChangeListener { view, b ->
                if (b) tvNombre.showDropDown()
            }

            val adapterAutoMarca =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, autocompletarMarca)
            tvMarca.threshold = 0
            tvMarca.setAdapter(adapterAutoMarca)
            tvMarca.setOnFocusChangeListener { view, b ->
                if (b) tvMarca.showDropDown()
            }
*/
            val adapterAutoCate =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, autocompletarCate)
            binding.tvCategoria.threshold = 0
            binding.tvCategoria.setAdapter(adapterAutoCate)
            binding.tvCategoria.setOnFocusChangeListener { view, b ->
                if (b) binding.tvCategoria.showDropDown()
            }
        })

    }

    // para enviar los push a los usuarios..............................
    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = Retrofitinstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.e(TAG, "response: ${Gson().toJson(response)} ")
                } else {
                    Log.e(TAG, response.errorBody().toString())

                }

            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }


        }

    /*fun lanzarPush() {
        val title = tvNombre.text.toString()
        val message = tvMarca.text.toString() + "  " + tvCategoria.text.toString()
        if (title.isNotEmpty() && message.isNotEmpty()) {
            PushNotification(
                NotificationData(title, message),
                TOPIC
            ).also {
                sendNotification(it)
            }
        }
    }
*/
    override fun onClick(v: View?) {
        if (v === binding.imageViewSub)
            showFilerChooser()

    }
}