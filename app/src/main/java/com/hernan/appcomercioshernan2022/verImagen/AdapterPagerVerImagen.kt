package com.hernan.appcomercioshernan2022.verImagen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ItemVerimagenBinding

class AdapterPagerVerImagen(var arrayVerImagen:ArrayList<String>, val context:FragmentActivity):RecyclerView.Adapter<AdapterPagerVerImagen.ViewHolderVer>() {

inner class ViewHolderVer(itemView: View):RecyclerView.ViewHolder(itemView){
    val binding = ItemVerimagenBinding.bind(itemView)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderVer =
        ViewHolderVer(LayoutInflater.from(parent.context).inflate(R.layout.item_verimagen, parent, false))


    override fun onBindViewHolder(holder: ViewHolderVer, position: Int) {
        val imagenZoom = arrayVerImagen[position]



        Glide.with(context.applicationContext).load(imagenZoom).into(holder.binding.zoomageViewVer)
    }

    override fun getItemCount(): Int = arrayVerImagen.size
}