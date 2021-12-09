package com.hernan.appcomercioshernan2022.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.modelos_de_datos.Dependiente
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ItemDependienteBinding
import java.math.BigDecimal
import java.math.RoundingMode

class RecyDependienteAdapter(var arrayDependiente:ArrayList<Dependiente>, val fragment:FragmentActivity):RecyclerView.Adapter<RecyDependienteAdapter.DepenViewHolder>() {

    inner class DepenViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val binding = ItemDependienteBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepenViewHolder =
        DepenViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_dependiente, parent, false))
    override fun getItemCount(): Int = arrayDependiente.size

    override fun onBindViewHolder(holder: DepenViewHolder, position: Int) {
        val arrayDep = arrayDependiente[position]
        val redondeo = BigDecimal(arrayDep.precio).setScale(2, RoundingMode.HALF_EVEN)

        holder.binding.textDependienteProducto.text = arrayDep.nombre + "  " +arrayDep.marca
        holder.binding.textDependientePrecio.text = "$ $redondeo"
        holder.binding.textDependienteMarca.text = arrayDep.marca
        Glide.with(fragment).load(arrayDep.imagen).into(holder.binding.imageviewDependiente)



    }



}
