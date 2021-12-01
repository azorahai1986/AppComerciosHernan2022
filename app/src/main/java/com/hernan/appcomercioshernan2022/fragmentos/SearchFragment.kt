package com.hernan.appcomercioshernan2022.fragmentos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hernan.appcomercioshernan2022.adapters.AdapterSearch
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria
import com.hernan.appcomercioshernan2022.databinding.FragmentSearchBinding
import com.hernan.appcomercioshernan2022.inicio.AdapterRecyclerPrincipal


class SearchFragment : Fragment() {
    var adapter: AdapterSearch? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var recyclerView: RecyclerView? = null

    var isOpen = true // para las animcaiones de los botones

    lateinit var binding: FragmentSearchBinding


    private val viewModel:MainViewModelo by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.edtActSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.e("dentro del editText", s.toString())
                adapter?.filtrado(s.toString())


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })



        observeData()
        inflarRecyclerBusqueda()
        return binding.root
    }
    fun inflarRecyclerBusqueda(){
        recyclerView = binding.recyclerBusqueda
        layoutManager = GridLayoutManager(context, 1)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        adapter = AdapterSearch(arrayListOf(), context as FragmentActivity)
        recyclerView?.adapter = adapter
    }
    fun observeData(){
        viewModel.fetchUserData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter!!.setData(it as ArrayList<ModeloDeIndumentaria>)
            adapter!!.notifyDataSetChanged()

        })

    }

}