package com.example.orangefintech.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.orangefintech.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PantallaPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantallaprincipal)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavMenu)
        val navController = findNavController(R.id.fragmentContainerView2)
        bottomNavigationView.setupWithNavController(navController)

    }
}