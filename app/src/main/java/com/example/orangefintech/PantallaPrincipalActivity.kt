package com.example.orangefintech

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.orangefintech.repositorios.UsuarioRepositorio
import com.google.android.material.bottomnavigation.BottomNavigationView

class PantallaPrincipalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_comprar -> {
                    true
                }
                R.id.item_saldo -> {
                    true
                }
                R.id.item_info -> {
                    true
                }
                else -> false
            }
        }
    }
}
