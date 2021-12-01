package com.hernan.appcomercioshernan2022.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.hernan.appcomercioshernan2022.actividades.MainActivity
import com.hernan.appcomercioshernan2022.databinding.FragmentAccederBinding
import com.hernan.appcomercioshernan2022.inicio.ProviderType


class AccederFragment : Fragment() {

    var botAcceder:Button? = null
    var tokenParaHome:String? = null
    lateinit var binding: FragmentAccederBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAccederBinding.inflate(inflater, container, false)


        setup()

        return binding.root
    }


    private fun setup(){
        val email = binding.etEmail.text
        val password = binding.etPassword.text
        binding.btAcceder?.setOnClickListener {


            if (binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(context, "usuario autenticado", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)

                        }else{
                            showAlertAcceder()

                        }

                    }.addOnFailureListener {
                        showAlert()
                    }

            }






        }
    }
    //crearé una alerta
    private fun showAlert(){
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage(" Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertAcceder(){
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage(" El Email ó la Contraseña son erroneos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
    }

   /* private fun showHome(email: String, provider: ProviderType){
        val homeIntent = Intent(context, MainActivity::class.java)
        homeIntent.putExtra("email", email)
        homeIntent.putExtra("provider", provider.name)
        startActivity(homeIntent)


    }*/

}