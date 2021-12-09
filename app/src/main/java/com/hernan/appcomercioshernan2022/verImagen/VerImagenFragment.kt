package com.hernan.appcomercioshernan2022.verImagen

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.hernan.appcomercioshernan2022.adapters.PagerSimilaresAdapter
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.PagerSimilares
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.actividades.MainActivity
import com.hernan.appcomercioshernan2022.databinding.DialogEditarBinding
import com.hernan.appcomercioshernan2022.databinding.DialogEditarImagenBinding
import com.hernan.appcomercioshernan2022.databinding.DialogEditarPrecioBinding
import com.hernan.appcomercioshernan2022.databinding.FragmentVerImagenBinding
import com.hernan.appcomercioshernan2022.inicio.InicioViewModel
import java.io.IOException
import java.util.*


class VerImagenFragment : Fragment() {
    var adapterSimilar: PagerSimilaresAdapter? = null
    var adapterZoom: AdapterPagerVerImagen? = null
    var viewPagerSimilar: ViewPager2? = null
    private val viewModel:MainViewModelo by viewModels()
    private lateinit var inicioViewModel: InicioViewModel

    var recibirImagen: String? = null
    var recibirImagenes: ArrayList<String>? = null
    var recibirNombre: String? = null
    var recibirMarca: String? = null
    var recibirPrecio: String? = null
    var recibirId: String? = null
    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    lateinit var storage: FirebaseStorage


// variable para recuperar el usuario
    private lateinit var auth: FirebaseAuth

    companion object {

        private const val IM_RECIBIDA = "IM_RECIBIDA" //para recibir la imagen del adapter
        private const val IMAGENES_RECIBIDAS = "IMAGENES_RECIBIDAS" //para recibir la imagen del adapter
        private const val NOMBRE_RECIBIDO = "NOMBRE_RECIBIDO" //para recibir el nombre del adapter
        private const val MARCA_RECIBIDA = "MARCA_RECIBIDA" //para recibir la descripcion del adapter
        private const val PRECIO_RECIBIDO = "PRECIO_RECIBIDO" //para recibir el precio del adapter
        private const val ID_RECIBIDO = "ID_RECIBIDO" //para recibir el precio del adapter y plasmarlo en el viewPager

        fun newInstance(
            recibirImagen: String,
            recibirImagenes: ArrayList<String>,
            recibirNombre: String,
            recibirMarca: String,
            precioRecibido: String,
            recibirId: String
        ): VerImagenFragment {

            val bundle = Bundle()
            bundle.putString(IM_RECIBIDA, recibirImagen)
            bundle.putSerializable(IMAGENES_RECIBIDAS, recibirImagenes)
            bundle.putString(NOMBRE_RECIBIDO, recibirNombre)
            bundle.putString(MARCA_RECIBIDA, recibirMarca)
            bundle.putString(PRECIO_RECIBIDO, precioRecibido)
            bundle.putString(ID_RECIBIDO, recibirId)
            val fragment = VerImagenFragment()
            fragment.arguments = bundle


            return fragment

        }

    }




    // variables para los elementos que usarè para eliminar o editar
    var nombre: TextView? = null
    var marca: TextView? = null
    var precio: TextView? = null
    var btEliminar: FloatingActionButton? = null
    lateinit var binding:FragmentVerImagenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVerImagenBinding.inflate(inflater, container, false)
//  del recycler principal
        storage = Firebase.storage

        // dare init a firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        recibirImagen = arguments?.getString(IM_RECIBIDA)
        recibirImagenes = arguments?.getSerializable(IMAGENES_RECIBIDAS) as ArrayList<String>?
        recibirNombre = arguments?.getString(NOMBRE_RECIBIDO)
        recibirMarca = arguments?.getString(MARCA_RECIBIDA)
        recibirPrecio = arguments?.getString(PRECIO_RECIBIDO)
        recibirId = arguments?.getString(ID_RECIBIDO)


        Log.e("IDVERIMAGENES", recibirId.toString())
        Log.e("IMAGENESVERIMAGEN", recibirImagenes.toString())

        binding.textViewNombre.text = recibirNombre
        binding.textviewMarca.text = recibirMarca
        binding.textViewPrecio.text = recibirPrecio
        //Glide.with(requireContext().applicationContext).load(recibirImagen!!).into(binding.imageviewVerImagen)

        inicioViewModel = ViewModelProvider(this)[InicioViewModel::class.java]
        inicioViewModel.mail.observe(viewLifecycleOwner, {
            inflarBottomNavBar(it.toString())

        })
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.edicion -> desbloquearEdicion()
                R.id.eliminar -> eliminarProducto()
            }
            true

        }

        observerDataSimil()
        inflarViewPagerSimilares()
        inflarPagerVerImegenes()







        return binding.root
    }
    fun inflarPagerVerImegenes(){
        Log.e("RecibMarca", recibirImagenes.toString())

        adapterZoom = AdapterPagerVerImagen(arrayListOf(), context as FragmentActivity)
        binding.viewPagerZoom.adapter = adapterZoom
        adapterZoom?.arrayVerImagen =recibirImagenes!!
        adapterZoom?.notifyDataSetChanged()
    }
    fun inflarViewPagerSimilares(){
        viewPagerSimilar = binding.viewPagerSimilares
        adapterSimilar = PagerSimilaresAdapter(arrayListOf(), context as FragmentActivity)
        viewPagerSimilar?.adapter = adapterSimilar
    }

    //  funcion para inflar el viewPager
    private fun observerDataSimil() {
        viewModel.fetchUserDataSimilares(recibirMarca).observe(
            this.viewLifecycleOwner,
            androidx.lifecycle.Observer {
                adapterSimilar!!.similaresArray = it as ArrayList<PagerSimilares>
                adapterSimilar!!.notifyDataSetChanged()
            })

    }


    fun inflarBottomNavBar(mail: String) {
        Log.e("VIEWMODEL IT", mail.toString())
        val nulo = "null"
        when {
            mail == nulo ->{ binding.bottomNav.isVisible = false
            }
            mail != null -> { binding.bottomNav.isVisible = true
            }
        }
    }

    fun desbloquearEdicion(){
        binding.textViewNombre.isClickable = true
        binding.textViewPrecio.isClickable = true
        binding.textViewNombre.setBackgroundResource(R.color.teal_700)
        binding.textViewPrecio.setBackgroundResource(R.color.teal_700)


        Toast.makeText(context, "toca los elementos resaltados con color para editarlos", Toast.LENGTH_LONG).show()
        binding.textViewNombre.setOnClickListener {
            dialogEditarNombre()

        }
        binding.textViewPrecio.setOnClickListener {
            dialogEditarPrecio()

        }
    }

    fun eliminarProducto() {
        FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
            .document(recibirId.toString())
            .delete().addOnSuccessListener {
                Toast.makeText(context, "Archivo Eliminado", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Falló", Toast.LENGTH_SHORT).show()

            }

        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)

    }

    fun editarNombre(etnom: EditText) {

        var nombrerecib = etnom.text.toString()

        if (nombrerecib.isNullOrEmpty()){
            Toast.makeText(context, "Cambia el nombre de tu producto", Toast.LENGTH_SHORT) .show()

        }else{
            var map = mutableMapOf<String, Any>()
            map["nombre"] = nombrerecib
            val editar = FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
                .document(recibirId.toString())
            editar.update(map)
                .addOnSuccessListener {
                    Toast.makeText(context, "Producto Modificado con exito", Toast.LENGTH_SHORT) .show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Falló Modificación", Toast.LENGTH_SHORT).show()

                }
            nombre?.text = nombrerecib
        }



    }
    fun dialogEditarNombre() {

        val dialogBinding = DialogEditarBinding.inflate(requireActivity().layoutInflater, null, false)
        //val dialogEdiNombre = LayoutInflater.from(context).inflate(R.layout.dialog_editar, null)
        val dialogConstructor = AlertDialog.Builder(context)
            .setView(dialogBinding.root)

        val etnom = dialogBinding.etNombre
        Log.e("EtNom", etnom.text.toString())
        editarNombre(etnom)

        //mostrar el dialog
        val alertDialog = dialogConstructor.show()
        dialogBinding.btCancelar.setOnClickListener {
            alertDialog.dismiss()

        }
        dialogBinding.btAceptar.setOnClickListener {

            if (etnom.text.isNullOrEmpty()){
                Toast.makeText(context, "Agrega un nombre ó cancela", Toast.LENGTH_SHORT)

            }else{
                editarNombre(etnom)
                alertDialog.dismiss()
                binding.textViewNombre.setBackgroundResource(R.color.hernanOscuro)
                binding.textViewPrecio.setBackgroundResource(R.color.hernanOscuro)

            }

        }
    }
    private fun dialogEditarPrecio() {


        val dialogBinding = DialogEditarPrecioBinding.inflate(requireActivity().layoutInflater, null, false)
        val dialogConstructor = AlertDialog.Builder(context).setView(dialogBinding.root)

        //dialogConstructor.create()

        val alertDialog = dialogConstructor.show()
        var edPrecio = dialogBinding.etPrecioDialog.text


        //mostrar el dialog
        dialogBinding.btCancelarPrecio.setOnClickListener {
            alertDialog.dismiss()

        }
        dialogBinding.btAceptarPrecio.setOnClickListener {
            Log.e("EdPrecio", edPrecio.toString())

            if (edPrecio.isNullOrEmpty()){
                Toast.makeText(context, "Agrega un Precio ó cancela", Toast.LENGTH_SHORT)

            }else{
                val map = mutableMapOf<String, Any>()
                map["precio"] = edPrecio.toString()
                val editar = FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
                    .document(recibirId.toString())
                editar.update(map)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Producto Modificado con exito", Toast.LENGTH_SHORT) .show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Falló Modificación", Toast.LENGTH_SHORT).show()

                    }
                binding.textViewPrecio.text = edPrecio.toString()
            }
            alertDialog.dismiss()
        }


    }
    fun dialogEditarImagen(){
        binding.viewPagerZoom.visibility = View.INVISIBLE
        binding.imageviewVerImagen.visibility = View.VISIBLE
        val dialogBinding = DialogEditarImagenBinding.inflate(requireActivity().layoutInflater, null, false)
        val dialogConstructor = AlertDialog.Builder(context).setView(dialogBinding.root)

        val alertDialog = dialogConstructor.show()
        dialogBinding.btCancelarImagen.setOnClickListener {
            alertDialog.dismiss()

        }

            dialogBinding.btAceptarImagen.setOnClickListener {
                uploadFile()
               alertDialog.dismiss()
            }
       

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                binding.imageviewVerImagen?.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (filePath != null){dialogEditarImagen()}
    }

    private fun uploadFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(requireContext())
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


                    var imagen = downloadUri.toString()

                    var map = mutableMapOf<String, Any>()
                    map["imagen"] = imagen

                    val edit = FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
                        .document(recibirId.toString())
                    edit.update(map)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Producto Modificado con exito",
                                Toast.LENGTH_SHORT
                            ) .show()
                            progressDialog.dismiss()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Falló Modificación", Toast.LENGTH_SHORT).show()

                        }


                } else {
                    // Handle failures
                    // ...
                }
            }

        }



    }


    private fun showFilerChooser() {
        //Log.e("URL iMAGEN", imagen.toString())

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)



    }
    /*fun onSNACK(view: View){
        //Snackbar(view)
        val snackbar = Snackbar.make(view, "edita los datos marcados con color",
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.LTGRAY)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.BLUE)
        textView.textSize = 18f
        snackbar.show()
    }*/

}