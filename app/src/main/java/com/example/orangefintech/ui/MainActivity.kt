package com.example.orangefintech.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.orangefintech.R
import com.example.orangefintech.databinding.ActivityMainBinding
import com.example.orangefintech.repositorios.UsuarioRepositorio
import com.example.orangefintech.ui.fragmentos.inicioFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonIngresar.setOnClickListener {

            val username = binding.editTextUsuario.text.toString()
            val password = binding.editTextPassword.text.toString()

            if(validateInput()) {
                if(UsuarioRepositorio.existe(username, password)) {

                    loguear(username, password)
                    binding.buttonIngresar.visibility = View.GONE

                } else {
                    Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor ingrese los datos solicitados", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCrearUser.setOnClickListener{

            crearUsuario()

        }
    }

    private fun loguear(username: String, password: String) {

        val loginMenu = Intent(this, PantallaPrincipalActivity::class.java).apply {
            putExtra("codigo", UsuarioRepositorio.iniciar(username, password).codigoCuenta)
            putExtra("nickname", UsuarioRepositorio.iniciar(username, password).nickname)
            putExtra("password", UsuarioRepositorio.iniciar(username, password).password)
        }
        startActivity(loginMenu)

        val fragmentoInicio = inicioFragment()
        val fragment: Fragment? =

        supportFragmentManager.findFragmentByTag(inicioFragment::class.java.simpleName)

        if(fragment !is inicioFragment) {
            supportFragmentManager.beginTransaction()
                .add(R.id.frameLayout, fragmentoInicio, inicioFragment::class.java.simpleName)
                .commit()
        }

    }

    private fun crearUsuario(){

        val crearUsuarioActivityIntent = Intent(this, CrearUsuarioActivity::class.java)
        startActivity(crearUsuarioActivityIntent)

    }

    fun validateInput(): Boolean {
        return binding.editTextUsuario.text.isNotEmpty() && binding.editTextPassword.text.isNotEmpty()
    }
}
