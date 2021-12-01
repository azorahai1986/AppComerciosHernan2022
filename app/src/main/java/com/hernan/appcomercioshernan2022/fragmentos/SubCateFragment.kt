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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hernan.appcomercioshernan2022.adapters.RecyclerSubcate
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.hernan.appcomercioshernan2022.modelos_de_datos.SubCategorias
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.actividades.ActividadAgregarSubCat


class SubCateFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var uidRecuperado:String? = null
    private var mailRecuperado:String? = null
    private var textViewC: TextView? = null

    var categoriaRecibida: String? = null
    var nombreCateRecibida:String? = null
    var btAgregarSub:FloatingActionButton? = null


    companion object {
        private const val CATE_RECIBIDA = "cate_recibida"
        private const val NOMCAT_RECIBIDA = "nomcat_recibida"
        const val VOLVERACATEGORIAS = "volver"
        fun newInstance(categoriaRecibidas: String, nombreCateReci:String): SubCateFragment {
            val bundle = Bundle()
            bundle.putString(CATE_RECIBIDA, categoriaRecibidas)
            bundle.putString(NOMCAT_RECIBIDA, nombreCateReci)

            val fragment = SubCateFragment()
            fragment.arguments = bundle
            return fragment

        }

    }
    private val viewModel:MainViewModelo by viewModels()
    var recyclerSub: RecyclerView? = null
    var adapterSub: RecyclerSubcate? = null
    var layoutManager:RecyclerView.LayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sub_cate, container, false)
        categoriaRecibida = arguments?.getString(CATE_RECIBIDA)
        nombreCateRecibida = arguments?.getString(NOMCAT_RECIBIDA)

        btAgregarSub = view.findViewById(R.id.fl_Bt_AgregarSub)
        textViewC = view.findViewById(R.id.text_sub)
        recyclerSub = view.findViewById(R.id.recy_subcate)

        layoutManager = GridLayoutManager(activity, 2)
        recyclerSub?.layoutManager = layoutManager
        recyclerSub?.setHasFixedSize(true)
        adapterSub = RecyclerSubcate(arrayListOf(), activity as FragmentActivity)
        recyclerSub?.adapter = adapterSub
        observeDataSub()

        auth = Firebase.auth
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            mailRecuperado = user.email

            Log.e("EmailHome", mailRecuperado.toString())

            uidRecuperado = user.uid

        }

        if (!(mailRecuperado.isNullOrEmpty())){
            btAgregarSub?.visibility = View.VISIBLE
            textViewC?.visibility = View.VISIBLE
        }

        btAgregarSub?.setOnClickListener { irAAgregarSub() }
        return view
    }

    private fun observeDataSub(){
        viewModel.fetchUserDataSubcate(categoriaRecibida).observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterSub!!.mutableListSub = it as ArrayList<SubCategorias>
            adapterSub!!.notifyDataSetChanged()
            Log.e("datos IT", it.toString())
        })

    }

    fun irAAgregarSub(){
        val intent = Intent(context, ActividadAgregarSubCat::class.java)
        intent.putExtra("id", categoriaRecibida)
        intent.putExtra("nombreCate", nombreCateRecibida)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        observeDataSub()
    }

}