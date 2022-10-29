package com.example.orangefintech.repositorios

import com.example.orangefintech.entidades.Compra
import com.example.orangefintech.entidades.Criptomonedas
import java.time.LocalDate
import java.time.LocalTime

object CompraRepositorio {
    val compra = mutableListOf<Compra>()

    init {
        CompraRepositorio.agregar(Compra("Andy", 0, LocalDate.of(2021,3,1), LocalTime.of(17,10,1), Criptomonedas.CRIPTODIA, 200.0, 10000.0, "2%"))
    }

    fun agregar(compra: Compra) {
        this.compra.add(compra)
    }

    fun obtenerListaCompraPorUsuario(nickname: String){

        var obtuvoInformacion = false

        for (elemento in compra) {
            if(elemento.usuario.equals(nickname)){
                val nombreCriptomoneda: String = stringuearTipoCripto(elemento.criptomoneda)
                println(
                    """
                     ---------------------------------------------   
                     Codigo de Compra: ${elemento.codigoCompra}
                     Fecha de Compra: ${elemento.fechaCompra}
                     Hora de Compra: ${elemento.horaCompra}
                     Criptomoneda: ${nombreCriptomoneda}
                     Valor Adquirido: $${elemento.valorAdquirido}
                     Valor Pagado: $${elemento.valorPagado}
                     Valor comision: ${elemento.comision} 
                     --------------------------------------------- 
                """.trimIndent()
                )
                obtuvoInformacion = true
            }
        }
        if(!obtuvoInformacion) println("No se han encontrado registros.\n")
    }

    private fun stringuearTipoCripto(criptomoneda: Criptomonedas): String {
        return when(criptomoneda){
            Criptomonedas.CRIPTOMAS -> "Criptomas"
            Criptomonedas.CRIPTODIA -> "Criptodia"
            Criptomonedas.CARRECRIPTO -> "Carrecripto"
        }
    }
}