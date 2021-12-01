package com.hernan.appcomercioshernan2022.adapters

import android.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.clases.AplicarPorcentajesPrecios
import com.example.navdrawer.clases.ModificarFirebase
import com.hernan.appcomercioshernan2022.fragmentos.dependienteFragment
import com.hernan.appcomercioshernan2022.modelos_de_datos.SubCategorias
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.DialogEditarBinding
import com.hernan.appcomercioshernan2022.databinding.DialogPorcentajeBinding
import com.hernan.appcomercioshernan2022.databinding.ItemPorcenSubBinding


class AdapterPorcenSubCate(var mutableListSub: ArrayList<SubCategorias>, val activity:FragmentActivity): RecyclerView.Adapter<AdapterPorcenSubCate.ViewHolderModel>() {



    inner class ViewHolderModel (itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemPorcenSubBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderModel =
        ViewHolderModel(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_porcen_sub, parent, false))

    override fun getItemCount(): Int = mutableListSub.size
    // Enlazar ViewHolder
    override fun onBindViewHolder(holder: ViewHolderModel, position: Int) {
        val modelSubPorcen = mutableListSub[position]

        var textViePorcentje= holder.binding.textviewPocen.text
        var cardViewPorcentaje = holder.binding.cardViewSubPorcen
        textViePorcentje = modelSubPorcen.marca
        holder.binding.textviewPocen.text = modelSubPorcen.porcentaje
        Glide.with(activity).load(modelSubPorcen.imagen).into(holder.binding.imageViewSub)

        if (!modelSubPorcen.porcentaje.isNullOrEmpty()){
            cardViewPorcentaje.setBackgroundResource(R.color.design_default_color_surface)
            textViePorcentje = modelSubPorcen.porcentaje + " %"
           
        }else{
            cardViewPorcentaje.setBackgroundResource(R.color.white)
            textViePorcentje = "0 %"


        }

        cardViewPorcentaje.setOnClickListener {
            dialogPorcentaje(modelSubPorcen.marca, modelSubPorcen.id)
        }





        /*cardViewPorcentaje.setOnLongClickListener {
            if (modelSubPorcen.check && modelSubPorcen.valor){
                cardViewPorcentaje.setBackgroundResource(R.color.WhiteColor)

                //holder.binding..visibility = View.GONE
                holder.binding.textviewPocen.visibility = View.VISIBLE


            }else{
                cardViewPorcentaje.setBackgroundResource(R.color.gris)
                /*holder.binding.editPorcen.visibility = View.VISIBLE
                porcenText.visibility = View.GONE
                porcenText.text = editPorcen.text
                porcenText.text = porcenDialog.edit_porcen.text*/


            }
            modelSubPorcen.check = !modelSubPorcen.check
            modelSubPorcen.valor = !modelSubPorcen.valor
            mutableListSub[position] = modelSubPorcen


            true

        }*/

        val dialog = LayoutInflater.from(activity).inflate(R.layout.dialog_porcentaje, null)



    }

    private fun dialogPorcentaje(marca: String, id: String) {


        val dialogPorcentaje = DialogPorcentajeBinding.inflate(activity.layoutInflater, null, false)
        val builder = AlertDialog.Builder(activity).setView(dialogPorcentaje.root)
        val alertDialog = builder.show()


        var editValor = dialogPorcentaje.edittextDialogPorcen.text
        dialogPorcentaje.tvDialogMarca .text = marca




            dialogPorcentaje.btDialogAceptar.setOnClickListener {
                if (editValor.isNullOrEmpty()){
                    val toast = Toast.makeText(activity, "Ingresa un valor", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK or Gravity.CENTER_HORIZONTAL, 0, 0)
                    toast.show()
                }else{
                    porcentajes(editValor.toString(), id)

                    irSubCateFragment(id, marca)
                    alertDialog.dismiss()

                    val aplicarPorcen = AplicarPorcentajesPrecios()
                    aplicarPorcen.userData(editValor.toString(), marca)
                }



            }



        dialogPorcentaje.btDialogCancelar.setOnClickListener {
            alertDialog.dismiss()

        }


    }
    fun porcentajes(porcen: String, id: String){
        val por = porcen + porcen.toDouble()/100

        val modificar = ModificarFirebase()
        modificar.modificarPorcentajes(porcen, id)

    }

    private fun irSubCateFragment(id: String, marca: String) {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, dependienteFragment.newInstance(marca, id))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(dependienteFragment.VOLVERPORCENTAJE)
            .commit()

    }

}