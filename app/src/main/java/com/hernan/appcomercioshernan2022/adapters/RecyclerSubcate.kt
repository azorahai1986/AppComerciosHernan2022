package com.hernan.appcomercioshernan2022.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hernan.appcomercioshernan2022.fragmentos.dependienteFragment
import com.hernan.appcomercioshernan2022.modelos_de_datos.SubCategorias
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ItemSubcateBinding

class RecyclerSubcate(var mutableListSub: ArrayList<SubCategorias>, val activity:FragmentActivity): RecyclerView.Adapter<RecyclerSubcate.ViewHolderModel>() {

    inner class ViewHolderModel (itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemSubcateBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderModel =
        ViewHolderModel(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subcate, parent, false))

    override fun getItemCount(): Int = mutableListSub.size
    // Enlazar ViewHolder
    override fun onBindViewHolder(holder: ViewHolderModel, position: Int) {
        val modelosFb = mutableListSub[position]

        holder.binding.textDescripcionSub.text = modelosFb.marca
        Glide.with(activity).load(modelosFb.imagen).into(holder.binding.imageViewSub)

        holder.binding.imageViewSub.setOnClickListener{
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, dependienteFragment.newInstance(modelosFb.marca, modelosFb.id))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(
                    dependienteFragment.VOLVERASUBCATE).commit()
        }

    }




}