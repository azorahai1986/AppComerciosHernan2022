package com.hernan.appcomercioshernan2022.verImagen

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.navdrawer.modelos_de_datos.PagerSimilares
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.actividades.MainActivity
import com.hernan.appcomercioshernan2022.adapters.PagerSimilaresAdapter
import com.hernan.appcomercioshernan2022.databinding.*
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.hernan.appcomercioshernan2022.enlace_con_firebase.crud_firestore.DeleteData
import com.hernan.appcomercioshernan2022.enlace_con_firebase.crud_firestore.EditFirestore
import com.hernan.appcomercioshernan2022.enlace_con_firebase.viewmodels_crud.ViewModelFirestore
import com.hernan.appcomercioshernan2022.inicio.InicioViewModel
import com.hernan.appcomercioshernan2022.pdf.Calculo
import com.hernan.appcomercioshernan2022.pdf.PdfFragment
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class VerImagenFragment : Fragment() {
    var adapterSimilar: PagerSimilaresAdapter? = null
    var adapterZoom: AdapterPagerVerImagen? = null
    var viewPagerSimilar: ViewPager2? = null
    private val viewModel:MainViewModelo by viewModels()
    private val viewModelFirestore:ViewModelFirestore by activityViewModels()
    private lateinit var inicioViewModel: InicioViewModel
    var editFirestor = EditFirestore()

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
    lateinit var binding: FragmentVerImagenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVerImagenBinding.inflate(inflater, container, false)
//  del recycler principal
        storage = Firebase.storage

        //Log.e("IDVERIMAGENES", recibirId.toString())

        val data = viewModelFirestore.dataFirestore.also {
            recibirNombre = it.nombre
            recibirImagen = it.imagen
            recibirMarca = it.marca
            recibirImagenes = it.arrayImagen
            recibirPrecio = it.precio
            recibirId = it.id
        }


        Log.e("IMAGENESVERIMAGEN", data.toString())

        ("$recibirNombre $recibirMarca").also { binding.textViewNombre.text = it }
        binding.textViewPrecio.text = recibirPrecio
        //Glide.with(requireContext().applicationContext).load(recibirImagen!!).into(binding.imageviewVerImagen)

        inicioViewModel = ViewModelProvider(this)[InicioViewModel::class.java]
        inicioViewModel.mail.observe(viewLifecycleOwner) {
            inflarBottomNavBar(it.toString())

        }
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.edicion -> desbloquearEdicion()
                R.id.eliminar -> eliminarProducto()
            }
            true

        }

        binding.cardSimil.setOnClickListener {
            //inflarFragment()
        }

        observerDataSimil()
        inflarViewPagerSimilares()
        inflarPagerVerImegenes()

        inflarSpinner()





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
        viewModel.fetchUserDataSimilares(recibirMarca).observe(viewLifecycleOwner,androidx.lifecycle.Observer {
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
        val delete = DeleteData()
        delete.deleteFirestore(recibirId!!, context)
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()

    }

    fun editarNombre(etnom: EditText) {

        var nombrerecib = etnom.text.toString()

        if (nombrerecib.isNullOrEmpty()){
            Toast.makeText(context, "Cambia el nombre de tu producto", Toast.LENGTH_SHORT) .show()

        }else{
            editFirestor.mapNombre(nombrerecib, recibirId, context)
            binding.textViewNombre.text = nombrerecib
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
                Toast.makeText(context, "Agrega un nombre ?? cancela", Toast.LENGTH_SHORT)

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
                Toast.makeText(context, "Agrega un Precio ?? cancela", Toast.LENGTH_SHORT)

            }else{
                editFirestor.mapPrecio(edPrecio.toString(), recibirId, context)
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
                            Toast.makeText(context, "Fall?? Modificaci??n", Toast.LENGTH_SHORT).show()

                        }


                } else {
                    // Handle failures
                    // ...
                }
            }

        }



    }

    private fun inflarSpinner(){

        val listCantidades = resources.getStringArray(R.array.cantidades)

        val adapterSpinner = ArrayAdapter(requireContext().applicationContext, android.R.layout.simple_spinner_dropdown_item, listCantidades)
        binding.spinnerCantidades.adapter = adapterSpinner

        binding.spinnerCantidades.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                when(listCantidades[p2]){
                     "0", "1", "2","3"->{
                         agregarPedido(listCantidades[p2])
                         "${listCantidades[p2]} unidades".also { binding.textCantidad.text = it }
                     }
                    "mayor cantidad" -> {

                        dialogSpinner()

                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }





    }
    fun dialogSpinner(){

        val dialogBinding = DialogSpinnerBinding.inflate(requireActivity().layoutInflater, null, false)
        val dialogConstructor = AlertDialog.Builder(context).setView(dialogBinding.root)

        val alertDialog = dialogConstructor.show()


        dialogBinding.textAceptar.setOnClickListener {

            val cantidad = dialogBinding.editCantidades.text.toString()
            agregarPedido(cantidad)


            alertDialog.dismiss()

        }

        dialogBinding.textCancelar.setOnClickListener {
            alertDialog.dismiss()
        }

    }


    fun agregarPedido(cantidades: String){
        val calculo = Calculo()
        val multi = calculo.multiplicar(cantidades.toInt(), recibirPrecio!!.toDouble())
        viewModelFirestore.dataFirestore.cantidad = cantidades
        viewModelFirestore.dataFirestore.subtotal = multi
        viewModelFirestore.dataFirestore.posicionEnLista = viewModelFirestore.dataListaCarrito.size+1


        binding.textSubtotal.text = multi
        binding.textCantidad.text = cantidades







        binding.cardAgregarCarrito.cardCarrito.setOnClickListener {

            if (cantidades == "0"){
                Toast.makeText(context, "Debes agregar por lo menos 1 unidad", Toast.LENGTH_SHORT).show()
            }else{
                viewModelFirestore.dataListaCarrito.add(viewModelFirestore.dataFirestore)
                viewModelFirestore.sumaArray.add(multi.toDouble())


                inflarFragmentPdf()
            }



            Log.e("Lista de viewmodels", viewModelFirestore.dataListaCarrito.toString())

        }


    }

    fun inflarFragmentPdf(){
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame_layout, PdfFragment())
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)?.commit()
    }


    private fun showFilerChooser() {
        //Log.e("URL iMAGEN", imagen.toString())

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)



    }

}