package com.example.orangefintech.repositorios

import com.example.orangefintech.entidades.Usuario
import java.time.LocalDate

object UsuarioRepositorio {

    val usuarios = mutableListOf<Usuario>()

    init {

        // Cuentas predefinidas

        usuarios.add(Usuario("Andy", "And1!", 0, "Andres", "Aquino", 2000.0, 0.0, LocalDate.of(2020, 1, 1)))
        usuarios.add(Usuario("Andres", "And1!", 1, "Andres", "Aquino", 5000.0, 0.0, LocalDate.of(2021, 2, 3)))
        usuarios.add(Usuario("Andresito", "And1!", 2, "Andres", "Aquino", 20000.0, 0.0, LocalDate.of(2022, 1, 1)))

    }

    fun agregar(usuario: Usuario) {
        if (existe(usuario.nickname, usuario.password)) println("Error al agregar la cuenta.")
        else usuarios.add(usuario)
    }

    /*//No usada pero la dejamos por las dudas
    fun eliminar(usuario: Usuario) {
        usuarios.remove(usuario)
    }*/

    fun existe(nickname: String, password: String): Boolean {
        return (usuarios.any { usuario: Usuario -> usuario.nickname.equals(nickname) && usuario.password.equals(password) });
    }

    fun iniciar(nickname: String, password: String): Usuario {
        var usuarioIniciado = Usuario()
        for (elemento in usuarios) {
            if (elemento.nickname.equals(nickname) && elemento.password.equals(password)) {
                usuarioIniciado = elemento
            }
        }
        return usuarioIniciado
    }

    fun obtenerPorCodigo(codigoCuenta: Int) {
        for (elemento in usuarios) {
            if (elemento.codigoCuenta.equals(codigoCuenta)) {
                print(
                    """
                   - Información de Usuario -
                   Codigo de Cuenta: ${elemento.codigoCuenta}
                   Nombre: ${elemento.nombre}
                   Apellido: ${elemento.apellido}
                   Dinero en cuenta: $${elemento.dineroEnCuenta}
                   Criptomonedas en cuenta: $${elemento.criptomonedasEnCuenta}
                   Fecha de alta: ${elemento.fechaAlta}
                   
                """.trimIndent()
                )
            }
        }
    }

    fun editarPorCodigo(codigoCuenta: Int, usuario: Usuario) {
        usuarios[codigoCuenta] = usuario
    }
}