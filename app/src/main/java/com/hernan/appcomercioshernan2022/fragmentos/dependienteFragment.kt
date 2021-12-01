package com.hernan.appcomercioshernan2022.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hernan.appcomercioshernan2022.adapters.RecyDependienteAdapter
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.Dependiente
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.actividades.ActividadAgregarProducto

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



class dependienteFragment : Fragment() {

    private val RECIBIRMARCA = "recibir_marca"
    private val RECIIBIR_ID = "recibir_id"

    private lateinit var auth: FirebaseAuth
    private var uidRecuperado:String? = null
    private var mailRecuperado:String? = null
    private var textViewP: TextView? = null


    private var recibirMarca: String? = null
    private var IdRecibido: String? = null

    companion object {

        const val VOLVERPORCENTAJE = "volverPorcentaje"
        const val VOLVERASUBCATE = "volver"
        fun newInstance(marcaRecibida: String, id: String) =
            dependienteFragment().apply {
                arguments = Bundle().apply {
                    putString(RECIBIRMARCA, marcaRecibida)
                    putString(RECIIBIR_ID, id)
                }
            }
    }
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    var recyDependiente:RecyclerView? = null
    var adapterDependiente: RecyDependienteAdapter? = null
    var layoutManager:RecyclerView.LayoutManager? = null
    var btAgregarProdu:FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        recibirMarca = arguments?.getString(RECIBIRMARCA)
        IdRecibido = arguments?.getString(RECIIBIR_ID)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dependiente, container, false)

        recyDependiente = view.findViewById(R.id.recycler_dependiente)
        layoutManager = GridLayoutManager(activity, 2)
        recyDependiente?.layoutManager = layoutManager
        recyDependiente?.setHasFixedSize(true)
        adapterDependiente = RecyDependienteAdapter(arrayListOf(), context as FragmentActivity)
        recyDependiente?.adapter = adapterDependiente

        observeDataDependiente()

        btAgregarProdu = view.findViewById(R.id.fl_Bt_AgregarProdu)
        textViewP = view.findViewById(R.id.text_p)

        auth = Firebase.auth
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            mailRecuperado = user.email

            uidRecuperado = user.uid

        }

        if (!(mailRecuperado.isNullOrEmpty())){
            btAgregarProdu?.visibility = View.VISIBLE
            textViewP?.visibility = View.VISIBLE
        }

        btAgregarProdu?.setOnClickListener { irAcitividadAgregarProducto() }

        return view
    }

    private fun observeDataDependiente(){
        viewModel.fetchUserDataDependiente(recibirMarca).observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterDependiente!!.arrayDependiente = it as ArrayList<Dependiente>
            adapterDependiente!!.notifyDataSetChanged()


        })


    }
    fun irAcitividadAgregarProducto(){
        val intent = Intent(context, ActividadAgregarProducto::class.java)
        intent.putExtra("id", IdRecibido)
        intent.putExtra("marca", recibirMarca)

        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        observeDataDependiente()
    }


}