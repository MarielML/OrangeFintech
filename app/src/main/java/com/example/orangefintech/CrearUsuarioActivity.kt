package com.example.orangefintech

import android.content.Intent
import android.media.MediaDataSource
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.orangefintech.entidades.Usuario
import java.time.LocalDate
import com.example.orangefintech.repositorios.UsuarioRepositorio

class CrearUsuarioActivity : AppCompatActivity() {

    private lateinit var et_usuario: EditText
    private lateinit var et_password: EditText
    private lateinit var et_confirmPw: EditText
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var btnCrearUsuario: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_usuario)

        et_usuario = findViewById(R.id.et_usuario)
        et_password = findViewById(R.id.et_password)
        et_confirmPw = findViewById(R.id.et_confirmPw)
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        btnCrearUsuario = findViewById(R.id.btnCrearUsuario)

        btnCrearUsuario.setOnClickListener {

            if(validateInput()) {

                val tiempoYDiaAhora: LocalDate = LocalDate.now()
                val nuevo = Usuario("", "", 0, "", "", 0.0, 0.0, tiempoYDiaAhora)
                val nuevoCodigoUsuario: Int = UsuarioRepositorio.usuarios.last().codigoCuenta.plus(1)
                nuevo.codigoCuenta = nuevoCodigoUsuario

                val usuario = et_usuario.text.toString()
                val password = et_password.text.toString()
                val confirmarPassword = et_confirmPw.text.toString()
                val nombre = etNombre.text.toString()
                val apellido = etApellido.text.toString()

                if (!password.equals(confirmarPassword)) {
                    Toast.makeText(this, "Las contraseñas no son iguales", Toast.LENGTH_SHORT)
                        .show()

                } else if (UsuarioRepositorio.existe(usuario, password)) {

                    Toast.makeText(
                        this,
                        "Nombre de usuario ya existe, cambielo",
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (!nuevo.comprobarContraseniaAlCrear(password)) {

                    Toast.makeText(
                        this,
                        "Contraseña debe tener al menos 1 mayus, 1 minus, 1 numero, 1 caracter especial",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    nuevo.nickname = usuario
                    nuevo.password = password
                    nuevo.nombre = nombre
                    nuevo.apellido = apellido
                    UsuarioRepositorio.agregar(nuevo)
                    Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }


            }
            else {

                Toast.makeText(this, "Por favor ingrese los datos solicitados", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun validateInput(): Boolean {
        return et_usuario.text.isNotEmpty() && et_password.text.isNotEmpty() && et_confirmPw.text.isNotEmpty() && etNombre.text.isNotEmpty() && etApellido.text.isNotEmpty()
    }

}