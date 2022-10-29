package com.example.orangefintech.entidades

import java.time.LocalDate
import java.time.LocalTime

data class Compra(

    val usuario: String,
    val codigoCompra: Int,
    val fechaCompra: LocalDate,
    val horaCompra: LocalTime,
    val criptomoneda: Criptomonedas,
    val valorAdquirido: Double,
    val valorPagado: Double,
    val comision: String
)