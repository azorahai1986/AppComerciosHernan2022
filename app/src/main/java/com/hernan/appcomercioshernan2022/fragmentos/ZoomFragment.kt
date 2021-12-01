package com.hernan.appcomercioshernan2022.fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.hernan.appcomercioshernan2022.adapters.PagerZoomAdapter
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.hernan.appcomercioshernan2022.databinding.FragmentZoomBinding


class ZoomFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }

    private var imagenesRecibidas: ArrayList<String>? = null
    private var idRecibido: String? = null
    private lateinit var binding: FragmentZoomBinding
    var adapterZoomm: PagerZoomAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentZoomBinding.inflate(inflater, container, false)
        imagenesRecibidas = arguments?.getSerializable(IMAGENES_RECIBIDAS) as ArrayList<String>?
        idRecibido = arguments?.getString(ID_RECIBIDO)
        Log.e("IDZOOM", idRecibido.toString())
        Log.e("IMAGENZOOM", imagenesRecibidas.toString())

        adapterZoomm = PagerZoomAdapter(arrayListOf(), context as FragmentActivity)
        binding.viewpagerZoom.adapter = adapterZoomm
        adapterZoomm?.arrayZoom = imagenesRecibidas!!
        adapterZoomm?.notifyDataSetChanged()

        if (adapterZoomm == null){
            binding.animacionZoom.visibility = View.VISIBLE
            binding.viewpagerZoom.visibility = View.GONE
        }else{
            binding.animacionZoom.visibility = View.GONE
            binding.viewpagerZoom.visibility = View.VISIBLE
        }

        //observeDataZoom()
        return binding.root
    }

    companion object {
        private val IMAGENES_RECIBIDAS = "IMAGENES_RECIBIDAS"
        private val ID_RECIBIDO = "IDRECIBIDO"

        const val VOLVERZOOM = "VOLVER"

        fun newInstance(imagenes: ArrayList<String>?, idRecibido: String?): ZoomFragment {

        val bundle = Bundle()
            bundle.putSerializable(IMAGENES_RECIBIDAS, imagenes)
            bundle.putString(ID_RECIBIDO, idRecibido)

            val fragment = ZoomFragment()
            fragment.arguments = bundle


            return fragment




        }
    }

}