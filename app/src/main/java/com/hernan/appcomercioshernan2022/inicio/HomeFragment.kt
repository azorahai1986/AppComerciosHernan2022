package com.hernan.appcomercioshernan2022.inicio

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.CartelPrincipal
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.actividades.CartelActivity
import com.hernan.appcomercioshernan2022.databinding.FragmentHomeBinding
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

enum class ProviderType {
    BIENVENIDO
}
class HomeFragment : Fragment() {

    private lateinit var inicioViewModel: InicioViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    var isOpen = true // para las animcaiones de los botones

    private var adapterRecyclerPrincipal: AdapterRecyclerPrincipal? = null
    private var adapterCartelPrincipal: PagerPrincipalAdapter? = null
    private var layoutManager:RecyclerView.LayoutManager? = null
    private var recyclerView:RecyclerView? = null
    private var viewPagerCartelPrincipal:ViewPager2? = null
    private val viewModel:MainViewModelo by viewModels()
    // para darle movimiento automatico al viewPager
    private var indicator:DotsIndicator? = null //indicador para el viewPager
    private var animationCartel:LottieAnimationView? = null
    private var animationRecycler:LottieAnimationView? = null
    private val handler = Handler()
    private  val runnable = Runnable {
        if (adapterCartelPrincipal?.itemCartel?.size != 0){
            viewPagerCartelPrincipal?.currentItem = if (viewPagerCartelPrincipal!!.currentItem == adapterCartelPrincipal!!.itemCartel.size-1) 0
            else viewPagerCartelPrincipal!!.currentItem+1

        }
    }
    lateinit var homeFragment: HomeFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        inicioViewModel =
            ViewModelProvider(this)[InicioViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textOfertas
        inicioViewModel.mail.observe(viewLifecycleOwner, {
            val emailUser = it
            instanciarVistas(emailUser)

        })


        inflarRecycler()
        inflarPager()
        observeData()
        cargarPagerCartelPrincipal()

        return root


    }
    fun inflarRecycler(){
        recyclerView = binding.recyclerProductos
        layoutManager = GridLayoutManager(activity, 1)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        adapterRecyclerPrincipal = AdapterRecyclerPrincipal(arrayListOf(), context as FragmentActivity)
        recyclerView?.adapter = adapterRecyclerPrincipal
    }
    fun inflarPager(){
        viewPagerCartelPrincipal = binding.viewpagerCartel
        indicator = binding.indicator
        animationCartel = binding.animacion2
        animationRecycler = binding.animacionRecycler


        adapterCartelPrincipal = PagerPrincipalAdapter(arrayListOf(), context as FragmentActivity)
        viewPagerCartelPrincipal!!.adapter = adapterCartelPrincipal
        indicator?.setViewPager2(viewPagerCartelPrincipal!!)// poner los puntitos delante del viewPager
        //darle el tempo para que corra el viewPager automaticamente

        viewPagerCartelPrincipal?.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 5000)
            }
        })

    }

    fun observeData(){
        viewModel.fetchUserData().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterRecyclerPrincipal!!.setData(it as ArrayList<ModeloDeIndumentaria>)
            adapterRecyclerPrincipal!!.notifyDataSetChanged()

           /* if (idRecibid != null){
                val i = busqueda()
                if (i>-1){
                    recyclerView!!.layoutManager!!.scrollToPosition(i)
                }
            }*/

        })

    }

    fun cargarPagerCartelPrincipal(){
        viewModel.fetchUserDataOfertas().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
        adapterCartelPrincipal!!.itemCartel = it as ArrayList<CartelPrincipal>
        adapterCartelPrincipal!!.notifyDataSetChanged()
            // para inflar y desaparecer las animaciones de carga
            val abrire = AnimationUtils.loadAnimation(context, R.anim.abrir2)

            if (adapterCartelPrincipal !=null){
                animationCartel?.visibility = View.GONE
                animationRecycler?.visibility = View.GONE
                viewPagerCartelPrincipal?.startAnimation(abrire)
            }
        })
    }

    fun irAlInicio(){
        homeFragment = HomeFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container_view_tag, homeFragment)
            ?.commit()
    }

    fun dialQuienSomos(){
        val dialogQuien =LayoutInflater.from(activity).inflate(R.layout.dialog_quienes_somos, null)
        val constructorDialog = AlertDialog.Builder(activity).setView(dialogQuien).setTitle("   Mercado y Comercio")
        // mostrar dialog.
        constructorDialog.show()
    }

    override fun onResume() {
        super.onResume()
        observeData()
        cargarPagerCartelPrincipal()

    }

    /*fun busqueda():Int{
        for (i in 0 until adapterRecyclerPrincipal!!.mutableListModel.size){
            if (adapterRecyclerPrincipal!!.mutableListModel[i].id == idRecibid){
                return i
            }
        }
        return -1
    }*/

    fun instanciarVistas(emailUser: String) {

        Log.e("INSTANCIARVISTA", emailUser)
        val rotate = AnimationUtils.loadAnimation(context, R.anim.rotar)
        val abrire = AnimationUtils.loadAnimation(context, R.anim.abrir)
        val cerrar = AnimationUtils.loadAnimation(context, R.anim.cerrar)


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun modoOscuroYClaro(){
        AppCompatDelegate.MODE_NIGHT_NO

    }
}