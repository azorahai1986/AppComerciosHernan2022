package com.hernan.appcomercioshernan2022.pdf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.FragmentPdfBinding
import com.hernan.appcomercioshernan2022.enlace_con_firebase.viewmodels_crud.ViewModelFirestore
import com.hernan.appcomercioshernan2022.inicio.AdapterRecyclerPrincipal
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria


class PdfFragment : Fragment() {
    private lateinit var binding:FragmentPdfBinding
    private val viewModelFirestore:ViewModelFirestore by activityViewModels()
    var layoutManager:RecyclerView.LayoutManager? = null
    var adapter:AdapterPdf? = null
    var adapterPdf:Pdf? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPdfBinding.inflate(inflater, container, false)

        Log.e("Lista de viewmodels pdf", viewModelFirestore.dataListaCarrito.toString())



        inflarRecycler()
        return binding.root
    }
    private fun inflarRecycler(){

        val data = viewModelFirestore.dataListaCarrito
        layoutManager = LinearLayoutManager(context)
        binding.recyclerListaCompra.layoutManager = layoutManager
        adapter = AdapterPdf(data as ArrayList<ModeloDeIndumentaria>)
        binding.recyclerListaCompra.adapter = adapter
    }


}