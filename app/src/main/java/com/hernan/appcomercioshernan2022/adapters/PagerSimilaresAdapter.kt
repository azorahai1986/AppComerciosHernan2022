package com.hernan.appcomercioshernan2022.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.modelos_de_datos.PagerSimilares
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ItemPagerSimilaresBinding
import java.math.BigDecimal
import java.math.RoundingMode

class PagerSimilaresAdapter(var similaresArray:ArrayList<PagerSimilares>, val activity:FragmentActivity):
    RecyclerView.Adapter<PagerSimilaresAdapter.SimilaresViewHolder>() {

    inner class SimilaresViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val binding = ItemPagerSimilaresBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilaresViewHolder =
        SimilaresViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pager_similares, parent, false))


    override fun getItemCount(): Int= similaresArray.size

    override fun onBindViewHolder(holder: SimilaresViewHolder, position: Int) {
        val arraySimilar= similaresArray[position]

        val precio = arraySimilar.precio.toDouble()
        val redondeo = BigDecimal(precio).setScale(2, RoundingMode.HALF_EVEN)

        holder.binding.textNombreSimilar.text = arraySimilar.nombre
        holder.binding.textPrecioSimilar.text = " $ $redondeo x unidad"
        holder.binding.textSubSimilar.text = arraySimilar.marca
        Glide.with(activity).load(arraySimilar.imagen).into(holder.binding.imageViewSimilar)
    }
}