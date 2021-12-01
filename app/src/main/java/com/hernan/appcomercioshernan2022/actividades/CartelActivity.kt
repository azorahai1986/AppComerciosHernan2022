package com.hernan.appcomercioshernan2022.actividades

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.hernan.appcomercioshernan2022.inicio.PagerPrincipalAdapter
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.CartelPrincipal
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ActivityCartelBinding
import java.io.IOException
import java.util.*

class CartelActivity : AppCompatActivity(), View.OnClickListener {


    private val PICK_IMAGE_REQUEST = 1234
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    private var adapter: PagerPrincipalAdapter? = null

    // Método para subir imagenes al firebase storage
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    lateinit var binding: ActivityCartelBinding
    override fun onClick(v: View?) {
        if (v === binding.imageCrarOfertas)
            showFilerChooser()
        else(v === binding.btCargarOfertas)
        uploadFile()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.imageCrarOfertas!!.setImageBitmap(bitmap)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }

    }

    private fun uploadFile() {
        if (filePath != null){
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Cargando...")
            progressDialog.show()

            // para modificar los datos de una lista usando firestore..........................

            val imageRef = storageReference!!.child("images/"+ UUID.randomUUID().toString())

            var uploadTask = imageRef.putFile(filePath!!)
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

                    //var precio = edit_text_precio_ofertas.text.toString()
                    var imagen = downloadUri.toString()

                    var map = mutableMapOf<String,Any>()
                    // map["id"] = id
                    //map["precioOferta"] = precio
                    map["imagen"] = imagen


                    FirebaseFirestore.getInstance().collection("NuevoModelo")
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
        binding = ActivityCartelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        storage = Firebase.storage

        // dare init a firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        // dar funcion al boton e imagen..............................
        var cargarImagen = findViewById<ImageView>(R.id.image_crar_ofertas)
        var cargarOferta = findViewById<ImageView>(R.id.bt_cargar_ofertas)
        cargarImagen.setOnClickListener(this)
        cargarOferta.setOnClickListener(this)

        exTraerDatos()

    }

    fun exTraerDatos(){
        viewModel.fetchUserDataOfertas().observe(this, androidx.lifecycle.Observer {
            adapter?.itemCartel = it as ArrayList<CartelPrincipal>
            adapter?.notifyDataSetChanged()
        })
    }

}