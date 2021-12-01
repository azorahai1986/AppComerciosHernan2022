package com.hernan.appcomercioshernan2022.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hernan.appcomercioshernan2022.adapters.AdapterPorcenSubCate
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.hernan.appcomercioshernan2022.modelos_de_datos.SubCategorias
import com.hernan.appcomercioshernan2022.databinding.FragmentPorcentajeBinding


class PorcentajeFragment : Fragment() {

    var porcenAdapter: AdapterPorcenSubCate? = null
    var layoutManager:RecyclerView.LayoutManager? = null
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }

    private lateinit var binding: FragmentPorcentajeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentPorcentajeBinding.inflate(inflater, container, false)

        layoutManager = GridLayoutManager(activity, 1)
        binding.recyclerPorcentaje.layoutManager = layoutManager
        binding.recyclerPorcentaje.setHasFixedSize(true)
        porcenAdapter = AdapterPorcenSubCate(arrayListOf(), activity as FragmentActivity)
        binding.recyclerPorcentaje.adapter = porcenAdapter

        observeDataSub()

//:::.......  dar funcion al bottomNav.................................


        /*binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.guardar -> {
                    Toast.makeText(requireContext(), "guardar", Toast.LENGTH_LONG).show()
                    verif = true
                    //porcenAdapter!!.dialogPorcentaje("")
                    true
                }
                R.id.cancelar -> {
                    Toast.makeText(requireContext(), "cancelar", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }*/

        return binding.root

    }
    private fun observeDataSub(){
        viewModel.fetchUserDataPorcentaje().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            porcenAdapter!!.mutableListSub = it as ArrayList<SubCategorias>
            porcenAdapter!!.notifyDataSetChanged()
        })

    }
    companion object {
        const val VOLVERPORCEN = "volverPorcen"


    }

    override fun onResume() {
        super.onResume()
        observeDataSub()
    }


}