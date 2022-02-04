package com.hernan.appcomercioshernan2022.actividades

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hernan.appcomercioshernan2022.adapters.RecyclerSubcate
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.hernan.appcomercioshernan2022.modelos_de_datos.SubCategorias
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ActivityActividadAgregarSubCatBinding
import java.io.IOException
import java.util.*

class ActividadAgregarSubCat : AppCompatActivity(), View.OnClickListener {

    val TAG = "ActividadAgregar"
    var btCargarSub: FloatingActionButton? = null
    var idRecibido:String? = null
    var nombreRecibido:String? = null
    var textViewNombreCate:TextView? = null
    private val PICK_IMAGE_REQUEST = 1234
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    private var adapter: RecyclerSubcate? = null

    // Método para subir imagenes al firebase storage
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    lateinit var binding: ActivityActividadAgregarSubCatBinding


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

                    val bundle = intent.extras
                    idRecibido = bundle?.getString("id")

                    var sub = binding.tvSubcate.text.toString()
                    var imagen = downloadUri.toString()

                    var map = mutableMapOf<String, Any>()
                     map["cate"] = idRecibido.toString()
                   // map["cate"] = cate
                    map["marca"] = sub
                    map["imagen"] = imagen




                    FirebaseFirestore.getInstance().collection("Subcatergoria")
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
        binding = ActivityActividadAgregarSubCatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        storage = Firebase.storage

        // dare init a firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference


        btCargarSub = binding.btCargarSubcate
        textViewNombreCate = binding.tvCategoria

        val bundle = intent.extras
        nombreRecibido = bundle?.getString("nombreCate")

        textViewNombreCate?.text = nombreRecibido

        binding.imageViewSub.setOnClickListener(this)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        btCargarSub?.setOnClickListener { uploadFile() }

        exTraerDatos()


    }

    fun exTraerDatos() {
        viewModel.fetchUserDataSubcate(idRecibido).observe(this, androidx.lifecycle.Observer {
            adapter?.mutableListSub = it as ArrayList<SubCategorias>
            adapter?.notifyDataSetChanged()
            val autocompletar = mutableListOf<String>()
            val autocompletarMarca = mutableListOf<String>()
            val autocompletarCate = mutableListOf<String>()

            for (x in it) {
                //autocompletar.add(x.nombre)
                autocompletarMarca.add(x.marca)
                autocompletarCate.add(x.cate)

            }

            val adapterAutoMarca =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, autocompletarMarca)
            binding.tvSubcate.threshold = 0
            binding.tvSubcate.setAdapter(adapterAutoMarca)
            binding.tvSubcate.setOnFocusChangeListener { view, b ->
                if (b) binding.tvSubcate.showDropDown()
            }
            /*val adapterAuto = ArrayAdapter(this, android.R.layout.simple_list_item_1, autocompletar)
            tvNombre.threshold = 0
            tvNombre.setAdapter(adapterAuto)
            tvNombre.setOnFocusChangeListener { view, b ->
                if (b) tvNombre.showDropDown()
            }

            */

            /*val adapterAutoCate =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, autocompletarCate)
            tvCategoria.threshold = 0
            tvCategoria.setAdapter(adapterAutoCate)
            tvCategoria.setOnFocusChangeListener { view, b ->
                if (b) tvCategoria.showDropDown()
            }*/
        })

    }


    override fun onClick(v: View?) {
        if (v === binding.imageViewSub)
            showFilerChooser()


    }

    override fun onResume() {
        super.onResume()
        exTraerDatos()
    }
}