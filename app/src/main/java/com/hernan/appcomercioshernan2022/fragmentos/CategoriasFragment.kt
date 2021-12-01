package com.hernan.appcomercioshernan2022.fragmentos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hernan.appcomercioshernan2022.adapters.CategoriasAdapter
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.hernan.appcomercioshernan2022.modelos_de_datos.Categorias
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hernan.appcomercioshernan2022.actividades.ActividadAgregar
import com.hernan.appcomercioshernan2022.databinding.FragmentCategoriasBinding


class CategoriasFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var uidRecuperado:String? = null
    private var mailRecuperado:String? = null
    companion object{
        const val volver = "VolverAlInicio"
    }


    private val viewModel:MainViewModelo by viewModels()
    private lateinit var binding:FragmentCategoriasBinding
    var layoutManager:RecyclerView.LayoutManager? = null
    var categoriasAdapter: CategoriasAdapter? = null
    var recyclerCategorias:RecyclerView? = null

    var btAgregarCate:FloatingActionButton?= null
    private var textViewC:TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriasBinding.inflate(inflater, container, false)

        btAgregarCate = binding.flBtAgregarCate
        textViewC = binding.textViewC


        // dar funcion a los botones.........................................
        auth = Firebase.auth
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            mailRecuperado = user.email

            Log.e("EmailHome", mailRecuperado.toString())

            uidRecuperado = user.uid

        }

        if (!(mailRecuperado.isNullOrEmpty())){
            btAgregarCate?.visibility = View.VISIBLE
            textViewC?.visibility = View.VISIBLE
        }
        btAgregarCate?.setOnClickListener { irAgregarActivity() }


        inflarRecyclerCategorias()
        observerDataCategorias()
        return binding.root
    }

    fun inflarRecyclerCategorias(){
        recyclerCategorias = binding.recyclerCategorias

        layoutManager = GridLayoutManager(activity, 2)
        recyclerCategorias?.layoutManager = layoutManager
        recyclerCategorias?.setHasFixedSize(true)
        categoriasAdapter = CategoriasAdapter(arrayListOf(), context as FragmentActivity)
        recyclerCategorias?.adapter = categoriasAdapter
    }

    private fun observerDataCategorias(){
        
        viewModel.fetchUserDataCategorias().observe(this.viewLifecycleOwner, Observer {
            categoriasAdapter?.arrayCategorias = it as ArrayList<Categorias>
            categoriasAdapter?.notifyDataSetChanged()

        } )
    }
    fun irAgregarActivity(){

        val intent = Intent(context, ActividadAgregar::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        observerDataCategorias()
    }






}