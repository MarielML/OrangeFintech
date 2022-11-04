package com.example.orangefintech.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.orangefintech.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PantallaPrincipalActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantallaprincipal)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavMenu)
        val navController = findNavController(R.id.fragmentContainerViewPantallaPrincipal)
        bottomNavigationView.setupWithNavController(navController)

        spinner = findViewById(R.id.spinnerExchanges)
        ArrayAdapter.createFromResource(this, R.array.exchange_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
        var exchange = spinner.selectedItem.toString()
        when(exchange){
            "Criptomas" -> ""
            "Criptodia" -> ""
            "Carrecripto" -> ""
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")

    }
}
