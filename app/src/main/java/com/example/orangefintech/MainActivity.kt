package com.example.orangefintech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.orangefintech.entidades.Usuario
import com.example.orangefintech.repositorios.UsuarioRepositorio

class MainActivity : AppCompatActivity() {

    private lateinit var etUsuario : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnIngresar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsuario = findViewById(R.id.editTextUsuario)
        etPassword = findViewById(R.id.editTextPassword)
        btnIngresar = findViewById(R.id.buttonIngresar)

        btnIngresar.setOnClickListener {
            if(validateInput()) {
                val usuario = UsuarioRepositorio.existe(etUsuario.text.toString(), etPassword.text.toString())

                if(usuario != null) {
                    val mainActivityIntent = Intent(this, MainActivity::class.java)

                    startActivity(mainActivityIntent)
                } else {
                    Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor ingrese los datos solicitados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(): Boolean {
        return etUsuario.text.isNotEmpty() && etPassword.text.isNotEmpty()
    }
}
