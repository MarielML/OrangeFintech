package com.example.orangefintech.entidades

import com.example.orangefintech.excepciones.SaldoInsuficiente
import java.time.LocalDate

class Usuario (

    var nickname: String= "",
    var password:String = "",
    var codigoCuenta: Int = 0,
    var nombre: String = "",
    var apellido: String = "",
    var dineroEnCuenta: Double = 0.0,
    var criptomonedasEnCuenta: Double = 0.0,
    val fechaAlta: LocalDate = LocalDate.now()

){

    // Funciones - Creacion de cuenta
    fun comprobarContraseniaAlCrear(contrasenia: String): Boolean {

        var contieneMayuscula = false
        var contieneMinuscula = false
        var contieneNumero = false
        var contieneCaracterEspecial = false

        /* Pasar todo el String a un char de Array e ir comprobando en un for cada letra
         Por ejemplo si contiene mayus su booleano es true. Al final de la comprobacion
         tiene que ser todos true. */

        val arrayCharContrasenia = contrasenia.toCharArray()

        for(i in arrayCharContrasenia.indices){

            if(arrayCharContrasenia[i].isDigit()) contieneNumero = true
            else if(arrayCharContrasenia[i].isUpperCase()) contieneMayuscula = true
            else if(arrayCharContrasenia[i].isLowerCase()) contieneMinuscula = true
            else if(!arrayCharContrasenia[i].isLetterOrDigit()) contieneCaracterEspecial = true

            if(contieneMayuscula && contieneMinuscula && contieneNumero && contieneCaracterEspecial) {
                this.password = contrasenia;
                return true
            }
        }
        return false
    }

    fun agregarSaldo(saldo: Double): Boolean{ if(saldo <= 0) return false else this.dineroEnCuenta += saldo; return true; }


    fun checkDineroACambiar(dinero: Double): Boolean{
        if(dinero > dineroEnCuenta || dinero <= 0){
            throw SaldoInsuficiente("Saldo insuficiente")
        } else {
            return true
        }
    }

}