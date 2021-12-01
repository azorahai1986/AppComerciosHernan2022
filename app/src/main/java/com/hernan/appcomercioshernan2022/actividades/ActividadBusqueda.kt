package com.hernan.appcomercioshernan2022.actividades

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.hernan.appcomercioshernan2022.fragmentos.SearchFragment
import com.hernan.appcomercioshernan2022.R
import com.hernan.appcomercioshernan2022.databinding.ActivityActividadBusquedaBinding


class ActividadBusqueda : AppCompatActivity() {

    lateinit var bindingActivity:ActivityActividadBusquedaBinding

    lateinit var busquedaFragment: SearchFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivity = ActivityActividadBusquedaBinding.inflate(layoutInflater)
        setContentView(bindingActivity.root)

        busquedaFragment = SearchFragment()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        inflarFragment()


    }

    private fun inflarFragment() {
        busquedaFragment = SearchFragment()
        supportFragmentManager.beginTransaction().replace(R.id.containerSearch, busquedaFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

    }


}