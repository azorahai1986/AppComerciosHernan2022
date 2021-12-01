package com.hernan.appcomercioshernan2022.actividades

import android.app.UiModeManager
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar.make
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ActivityMainBinding
import com.hernan.appcomercioshernan2022.fragmentos.AccederFragment
import com.hernan.appcomercioshernan2022.fragmentos.CategoriasFragment
import com.hernan.appcomercioshernan2022.fragmentos.PorcentajeFragment
import com.hernan.appcomercioshernan2022.inicio.HomeFragment
import java.net.URLEncoder

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var emailUser:String = "visitante"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.appBarMain.toolbar, (
                    R.string.navigation_drawer_open), (R.string.navigation_drawer_close)
        ) {}

        drawerToggle.isDrawerIndicatorEnabled = true
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        inflarHomeFragment()
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){

            emailUser = currentUser.email.toString()
            Log.e("Email MAin ACtivity", emailUser.toString())

            Snackbar.make(binding.appBarMain.frameLayout, "Bienvenido $emailUser", Snackbar.LENGTH_LONG).show()
            binding.navView.menu.findItem(R.id.cerrar).isVisible = true
            binding.navView.menu.findItem(R.id.porcentajes).isVisible = true
            binding.navView.menu.findItem(R.id.acceder).isVisible = false
            binding.navView.menu.findItem(R.id.carteles).isVisible = true
        }else{
            Snackbar.make(binding.appBarMain.frameLayout, "Bienvenido $emailUser", Snackbar.LENGTH_LONG).show()
            binding.navView.menu.findItem(R.id.cerrar).isVisible = false
            binding.navView.menu.findItem(R.id.porcentajes).isVisible = false
            binding.navView.menu.findItem(R.id.acceder).isVisible = true
            binding.navView.menu.findItem(R.id.carteles).isVisible = false
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.inicio -> {
               supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment())
                   .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.categorias -> {
                supportFragmentManager.beginTransaction().
                setCustomAnimations(R.anim.abrir2, R.anim.cerrar).replace(R.id.frame_layout, CategoriasFragment()
                ).setReorderingAllowed(true)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit()


            }
            R.id.busqueda -> {
                val intent = Intent(this, ActividadBusqueda::class.java)
                startActivity(intent)


            }
            R.id.whatsapp ->{
                whatsapp()
            }
            R.id.carteles ->{
                val intent = Intent(this, CartelActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.expandir_lateral_derecho, R.anim.contraer_lateral_derecho)
            }

            R.id.acceder -> {
                Log.e("Email MAin Acceder", emailUser.toString())

                if (emailUser != "visitante") {
                    Toast.makeText(this, "Ya estás registrado", Toast.LENGTH_LONG).show()
                } else {

                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame_layout,
                        AccederFragment()
                    ).setReorderingAllowed(true)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit()
                }

            }
            R.id.cerrar -> {
                Log.e("Email MAin Cerrar", emailUser.toString())

                if (emailUser == "visitante") {
                    Toast.makeText(this, "No estás registrado", Toast.LENGTH_LONG).show()

                } else {
                    cerrarSesion()
                }

            }
            R.id.porcentajes -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.expandir_lateral, R.anim.contraer_lateral)
                    .replace(R.id.frame_layout, PorcentajeFragment())
                    .setReorderingAllowed(true)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(PorcentajeFragment.VOLVERPORCEN).commit()
            }


        }


        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    fun inflarHomeFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
    }

    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)

        finish()

    }
    private fun whatsapp(){


        val msj = "Cordiales saludos. solicito una conferencia informativa"
        val numeroTel = "+541133545454"

        val packageManager = this.packageManager
        val i = Intent(Intent.ACTION_VIEW)

        try {
            val url =
                "https://api.whatsapp.com/send?phone=$numeroTel&text=" + URLEncoder.encode(
                    msj,
                    "UTF-8"
                )
            Log.e("URL WHATS", url)
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            Log.e("DATA URI WHAT", i.data.toString())

            this.startActivity(i)
            Toast.makeText(this, "No tienes instalado Whatsapp", Toast.LENGTH_SHORT).show()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }







    




    
        
}