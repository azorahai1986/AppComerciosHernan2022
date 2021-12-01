package com.hernan.appcomercioshernan2022.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hernan.appcomercioshernan2022.fragmentos.SubCateFragment
import com.hernan.appcomercioshernan2022.modelos_de_datos.Categorias
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ItemCategoriasBinding

class CategoriasAdapter(var arrayCategorias:MutableList<Categorias>, val fragment:FragmentActivity):RecyclerView.Adapter<CategoriasAdapter.CateViewHolder>() {
    inner class CateViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val binding = ItemCategoriasBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CateViewHolder =
        CateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_categorias, parent, false))

    override fun getItemCount(): Int = arrayCategorias.size

    override fun onBindViewHolder(holder: CateViewHolder, position: Int) {
        val categoriasArray = arrayCategorias[position]
        holder.binding.textItemCategorias.text = categoriasArray.cate
        Glide.with(fragment).load(categoriasArray.imagen).into(holder.binding.imagenItemCategorias)

        holder.binding.cardviewCategorias.setOnClickListener {
            fragment.supportFragmentManager.beginTransaction().replace(R.id.frame_layout, SubCateFragment.newInstance(categoriasArray.id, categoriasArray.cate))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(null).commit()
        }


    }
}