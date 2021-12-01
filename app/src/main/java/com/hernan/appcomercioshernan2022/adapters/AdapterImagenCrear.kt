package com.hernan.appcomercioshernan2022.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ItemImagenescrearBinding


class AdapterImagenCrear(var arrayImagenes: ArrayList<Uri>):RecyclerView.Adapter<AdapterImagenCrear.ImagenesViewHolder>() {

    inner class ImagenesViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        val binding = ItemImagenescrearBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagenesViewHolder =
        ImagenesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_imagenescrear, parent, false))

    override fun getItemCount() = arrayImagenes.size

    override fun onBindViewHolder(holder: ImagenesViewHolder, position: Int) {
         var imagenes = arrayImagenes[position]
        Log.e("imagenadapter", imagenes.toString())

        holder.binding.imageViewProdu.setImageURI(imagenes)

    }


}