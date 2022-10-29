package com.example.orangefintech.entidades

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

abstract class Exchange{
    abstract fun calcularComision(): Double
}

object Criptomas: Exchange(){
    override fun calcularComision(): Double {
        return 0.02 // Comision del 2%
    }
}

object Criptodia: Exchange(){
    override fun calcularComision(): Double {
        return if(LocalTime.now() in LocalTime.of(20,0,0,0)..LocalTime.of(23,59,0,0)){
            0.01 // Comision del 1% entre las 20:00 y las 23:59
        } else{
            0.03 // Comision del 3%
        }
    }
}

object Carrecripto: Exchange(){
    override fun calcularComision(): Double {
        val diaDeHoy = LocalDate.now(); // Obtiene el dia de hoy
        return when(diaDeHoy.dayOfWeek){
            DayOfWeek.SATURDAY ->  0.03
            DayOfWeek.SUNDAY -> 0.03
            else -> 0.0075
        }
        // Si es sabado o domingo retorna 3% de comision
        // Cualquier otro dia retorna comision de 0.75%-
    }
}