package com.hernan.appcomercioshernan2022.inicio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.modelos_de_datos.CartelPrincipal
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ItemCartelBinding

class PagerPrincipalAdapter(var itemCartel:ArrayList<CartelPrincipal>, val fragment:FragmentActivity):RecyclerView.Adapter<PagerPrincipalAdapter.ViewHolderCartel>() {

    inner class ViewHolderCartel(itemView: View):RecyclerView.ViewHolder(itemView){
        val binding = ItemCartelBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCartel =
        ViewHolderCartel(LayoutInflater.from(parent.context).inflate(R.layout.item_cartel, parent, false))

    override fun getItemCount(): Int= itemCartel.size

    override fun onBindViewHolder(holder: ViewHolderCartel, position: Int) {
        val cartelArray = itemCartel[position]

        Glide.with(fragment).load(cartelArray.imagen).into(holder.binding.imageCartel)
    }


}