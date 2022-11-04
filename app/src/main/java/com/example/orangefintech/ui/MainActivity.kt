package com.example.orangefintech.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.orangefintech.R
import com.example.orangefintech.repositorios.UsuarioRepositorio

class MainActivity : AppCompatActivity() {

    private lateinit var etUsuario : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnIngresar : Button
    private lateinit var btnCrearUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsuario = findViewById(R.id.editTextUsuario)
        etPassword = findViewById(R.id.editTextPassword)
        btnIngresar = findViewById(R.id.buttonIngresar)
        btnCrearUser = findViewById(R.id.btnCrearUser)

        btnIngresar.setOnClickListener {

            val username =  etUsuario.text.toString()
            val password = etPassword.text.toString()

            if(validateInput()) {
                if(UsuarioRepositorio.existe(username, password)) {

                    loguear()

                } else {
                    Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor ingrese los datos solicitados", Toast.LENGTH_SHORT).show()
            }
        }

        btnCrearUser.setOnClickListener{

            crearUsuario()

        }
    }

    private fun loguear() {

        val loginMenu = Intent(this, PantallaPrincipalActivity::class.java)
        startActivity(loginMenu)

    }

    private fun crearUsuario(){

        val crearUsuarioActivityIntent = Intent(this, CrearUsuarioActivity::class.java)
        startActivity(crearUsuarioActivityIntent)

    }

    private fun validateInput(): Boolean {
        return etUsuario.text.isNotEmpty() && etPassword.text.isNotEmpty()
    }
}
