package com.hernan.appcomercioshernan2022.pdf

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.core.view.View
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ItemPdfBinding
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria

class AdapterPdf(val arrayPdf:ArrayList<ModeloDeIndumentaria>):RecyclerView.Adapter<AdapterPdf.ViewHolderPdf>(){
    inner class ViewHolderPdf(itemView:android.view.View):RecyclerView.ViewHolder(itemView){
        val binding = ItemPdfBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPdf =
        ViewHolderPdf(LayoutInflater.from(parent.context).inflate(R.layout.item_pdf, parent, false))


    override fun onBindViewHolder(holder: ViewHolderPdf, position: Int) {
        val arrayProductos = arrayPdf[position]
        holder.binding.textNombre.text = arrayProductos.nombre
        holder.binding.textPrecio.text = arrayProductos.precio
        holder.binding.textPrecio.text = arrayProductos.precio
    }

    override fun getItemCount(): Int = arrayPdf.size
}
