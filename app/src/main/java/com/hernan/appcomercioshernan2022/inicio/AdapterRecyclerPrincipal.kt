package com.hernan.appcomercioshernan2022.inicio

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hernan.appcomercioshernan2022.verImagen.VerImagenFragment
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ItemProductosBinding
import com.hernan.appcomercioshernan2022.enlace_con_firebase.viewmodels_crud.ViewModelFirestore
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue

class AdapterRecyclerPrincipal(var mutableListModel: ArrayList<ModeloDeIndumentaria>, val activity:FragmentActivity): RecyclerView.Adapter<AdapterRecyclerPrincipal.ViewHolderModel>() {

    private val viewModelFirestore:ViewModelFirestore by activity.viewModels()

    var arrayFiltro: ArrayList<ModeloDeIndumentaria> = ArrayList()
    fun setData(datos: ArrayList<ModeloDeIndumentaria>){
        mutableListModel = datos
        arrayFiltro = ArrayList(mutableListModel)

    }

    inner class ViewHolderModel (itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemProductosBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderModel =
        ViewHolderModel(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_productos, parent, false))

    override fun getItemCount(): Int = arrayFiltro.size
    // Enlazar ViewHolder
    override fun onBindViewHolder(holder: ViewHolderModel, position: Int) {
        val modelosFb = arrayFiltro[position]

        var precio = modelosFb.precio.toDouble()
        val redondeo = BigDecimal(precio).setScale(2, RoundingMode.HALF_EVEN)

        holder.binding.textviewNombre.text = modelosFb.nombre + " "+modelosFb.marca
        holder.binding.textviewPrecio.text = " $ $redondeo"
        holder.binding.textviewMarca.text = modelosFb.marca
        Glide.with(activity).load(modelosFb.imagen).into(holder.binding.imageview)



        holder.binding.imageview.setOnClickListener{

            viewModelFirestore.dataFirestore = modelosFb
            viewModelFirestore.position = position
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, VerImagenFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit()
        }

    }
    fun getIndex(mutableArray: ModeloDeIndumentaria): Int {
        var index = -1
        mutableListModel.forEachIndexed { i, p ->
            if(mutableArray.id == p.id) {
                index = i
                return@forEachIndexed
            }
        }
        return index
    }


}
