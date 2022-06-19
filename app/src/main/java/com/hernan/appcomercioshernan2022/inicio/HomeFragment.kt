package com.hernan.appcomercioshernan2022.inicio

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.example.navdrawer.modelos_de_datos.CartelPrincipal
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.FragmentHomeBinding
import com.hernan.appcomercioshernan2022.enlace_con_firebase.MainViewModelo
import com.hernan.appcomercioshernan2022.enlace_con_firebase.viewmodels_crud.ViewModelFirestore
import com.hernan.appcomercioshernan2022.firestore_corrutinas.ViewModelCorrutinas
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator


private const val ID_DOCUMENT = "idDocument"

class HomeFragment : Fragment() {
    private lateinit var inicioViewModel: InicioViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var idDocumet: String? = null

    lateinit var intent:Intent
    var isOpen = true // para las animcaiones de los botones

    private var adapterRecyclerPrincipal: AdapterRecyclerPrincipal? = null
    private var adapterCartelPrincipal: PagerPrincipalAdapter? = null
    private var layoutManager:RecyclerView.LayoutManager? = null
    private var recyclerView:RecyclerView? = null
    private var viewPagerCartelPrincipal:ViewPager2? = null
    private val viewModelo: MainViewModelo by viewModels()
    private val viewModel:ViewModelCorrutinas by viewModels()
    private val viewModelFirestore: ViewModelFirestore by activityViewModels()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idDocumet = it.getString(ID_DOCUMENT)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        inicioViewModel =
            ViewModelProvider(this)[InicioViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textOfertas
        inicioViewModel.mail.observe(viewLifecycleOwner) {
            val emailUser = it
            instanciarVistas(emailUser)

        }

        initObserverrs()
        inflarRecycler()
        inflarPager()
        cargarPagerCartelPrincipal()

        Log.e("ID DOCUMENT HOMEFRAg", idDocumet.toString())
        return root


    }
    fun inflarRecycler(){
        recyclerView = binding.recyclerProductos
        layoutManager = LinearLayoutManager(activity)
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
    fun indexarRecycler(): Int {

        for (i in 0 until adapterRecyclerPrincipal!!.arrayFiltro.size){

            if (adapterRecyclerPrincipal!!.arrayFiltro[i].id == idDocumet){
                return i
            }
        }
        return -1

    }
    fun posicionarRecycler(i: Int, arrayPrincipal: ArrayList<ModeloDeIndumentaria>?) {

        if (i > -1){
            if (arrayPrincipal != null){
                Log.e("index I ", i.toString())
                Log.e("ArrayPrincipal", arrayPrincipal.toString())


            }

        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun initObserverrs() {
        var indice = 0
        var arrayPos = ArrayList<Int>()
        viewModelo.fetchUserData().observe(viewLifecycleOwner) {
            adapterRecyclerPrincipal?.arrayFiltro = it as ArrayList<ModeloDeIndumentaria>


            adapterRecyclerPrincipal?.notifyDataSetChanged()


        }


        val position = viewModelFirestore.position
        binding.recyclerProductos.layoutManager?.scrollToPosition(position)
        Handler().postDelayed({ binding.recyclerProductos.smoothScrollToPosition(position) }, 500)
        binding.recyclerProductos.layoutManager?.layoutDirection
        binding.recyclerProductos.layoutManager?.offsetChildrenVertical(position)



    }


    fun cargarPagerCartelPrincipal(){
        viewModelo.fetchUserDataOfertas().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
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


    override fun onResume() {
        super.onResume()
        cargarPagerCartelPrincipal()

    }
    private fun removeObservers() {
        viewModel.firestoreData.removeObservers(this)
        viewModel.error.removeObservers(this)
        viewModel.removeListener()
    }
    override fun onStop() {
        super.onStop()
        removeObservers()
    }

    fun instanciarVistas(emailUser: String) {

        binding.tvVolverInicio.setOnClickListener {

            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame_layout, HomeFragment())
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)?.commit()

        }
       // Log.e("INSTANCIARVISTA", emailUser)
        val rotate = AnimationUtils.loadAnimation(context, R.anim.rotar)
        val abrire = AnimationUtils.loadAnimation(context, R.anim.abrir)
        val cerrar = AnimationUtils.loadAnimation(context, R.anim.cerrar)


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(idDoc: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_DOCUMENT, idDoc)
                }
            }
    }
    class SnappingLinearLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) :
        LinearLayoutManager(context, orientation, reverseLayout) {
        override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State, position: Int) {
            val smoothScroller: RecyclerView.SmoothScroller = TopSnappedSmoothScroller(recyclerView.context)
            smoothScroller.targetPosition = position
            Log.e("Encontrar posicion", smoothScroller.targetPosition.toString())
            startSmoothScroll(smoothScroller)
        }

        private inner class TopSnappedSmoothScroller(context: Context?) :
            LinearSmoothScroller(context) {
            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return this@SnappingLinearLayoutManager
                    .computeScrollVectorForPosition(targetPosition)
            }

            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    }

}