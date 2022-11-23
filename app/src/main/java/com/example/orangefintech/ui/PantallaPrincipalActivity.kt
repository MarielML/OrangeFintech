package com.example.orangefintech.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orangefintech.R
import com.example.orangefintech.adapters.ComprasAdapter
import com.example.orangefintech.entidades.*
import com.example.orangefintech.excepciones.SaldoInsuficiente
import com.example.orangefintech.repositorios.CompraRepositorio
import com.example.orangefintech.repositorios.UsuarioRepositorio
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period

class PantallaPrincipalActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private lateinit var spinner: Spinner
    private lateinit var inicio: Button
    private lateinit var cuenta: TextView
    private lateinit var comprar: Button
    private lateinit var monto: EditText
    private lateinit var etSaldo: EditText
    private lateinit var agregar: FloatingActionButton
    private lateinit var recyclerViewCompras: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantallaprincipal)

        val bundle = intent.extras
        val codigoDeCuenta: Int? = bundle?.getInt("codigo")
        val nickname: String? = bundle?.getString("nickname")
        val password: String? = bundle?.getString("password")
        val usuario: Usuario = UsuarioRepositorio.iniciar(nickname!!, password!!)
        etSaldo = findViewById<EditText>(R.id.etSaldo)
        val saldo = etSaldo.text.toString().toDouble()
        agregar = findViewById<FloatingActionButton>(R.id.floatingActionButtonSaldo)

        cuenta = findViewById<TextView>(R.id.tvCuenta)
        cuenta.text = UsuarioRepositorio.obtenerPorCodigo(codigoDeCuenta!!).toString()

        spinner = findViewById(R.id.spinnerExchanges)
        ArrayAdapter.createFromResource(this, R.array.exchange_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        comprar = findViewById(R.id.buttonComprar)
        comprar.setOnClickListener {
            monto = findViewById(R.id.etMonto)
            val usuarioNickname: String = usuario.nickname
            val nuevoCodigoCompra: Int = CompraRepositorio.compra.last().codigoCompra.plus(1)
            val fechaAhora: LocalDate = LocalDate.now()
            val horaAhora: LocalTime = LocalTime.now()
            val criptomonedaTipo = when(spinner.selectedItem.toString()) {
                "Criptomas" -> Criptomonedas.CRIPTOMAS
                "Criptodia" -> Criptomonedas.CRIPTODIA
                "Carrecripto" -> Criptomonedas.CARRECRIPTO
                else -> Toast.makeText(this, "Por favor selecciona un exchange", Toast.LENGTH_SHORT).show()
            }
            val dineroACambiar: Double = monto.text.toString().toDouble()
            val dineroTotal: Double = calcularDineroTotal(dineroACambiar, criptomonedaTipo as Exchange)
            try {
                if (usuario.checkDineroACambiar(dineroTotal) && (criptomonedaTipo == Criptomonedas.CRIPTOMAS || criptomonedaTipo == Criptomonedas.CRIPTODIA || criptomonedaTipo == Criptomonedas.CARRECRIPTO)) {
                    val comision: String = when(criptomonedaTipo) {
                        Criptomonedas.CRIPTOMAS -> "2%"
                        Criptomonedas.CRIPTODIA -> if (Criptodia.calcularComision() == 0.01) "1%" else "3%"
                        Criptomonedas.CARRECRIPTO -> if (Criptomas.calcularComision() == 0.03) "(3%)" else "(0.75%)"
                        else -> ""
                    }
                    val cashback: Double = dineroTotal.times(otorgarCashback(usuario))
                    val valorTotalCriptomonedas: Double = editarUsuarioAlComprar(monto.text.toString().toDouble(), dineroTotal, cashback, usuario)
                    val nuevaCompra = Compra(usuarioNickname, nuevoCodigoCompra, fechaAhora, horaAhora, criptomonedaTipo as Criptomonedas,
                        valorTotalCriptomonedas, dineroACambiar, comision)
                    CompraRepositorio.agregar(nuevaCompra)

                    var mensaje = """
                         COMPRA REALIZADA
                         Usted compró $valorTotalCriptomonedas Criptomonedas de ${spinner.selectedItem.toString()}
                         Se cobró una comisión de ${'$'} $comision
                    """.trimIndent()

                    mensaje += if (cashback != 0.0) """
                        Se otorgó un cashback de $cashback
                        Muchas gracias por la compra.
                    """.trimIndent()
                    else "Muchas gracias por la compra."
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                }

                UsuarioRepositorio.editarPorCodigo(codigoDeCuenta, usuario)

            } catch (e: SaldoInsuficiente) {
                Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show()
            }
        }

        agregar.setOnClickListener {
            agregarSaldo(usuario, saldo)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavMenu)
        val navController = findNavController(R.id.fragmentContainerViewPantallaPrincipal)
        bottomNavigationView.setupWithNavController(navController)

        inicio = findViewById(R.id.inicioFragment2)
        inicio.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_compraFragment2_to_perfilFragment22)
        }



    }

    private fun editarUsuarioAlComprar(dineroACambiar: Double, dineroTotal: Double, cashback: Double, usuario: Usuario): Double {
        val valorCriptomoneda = 1.0
        val valorDinero = 50.0

        val valorTotalCriptomonedas = (dineroACambiar.times(valorCriptomoneda)).div(valorDinero)
        usuario.criptomonedasEnCuenta = usuario.criptomonedasEnCuenta.plus(valorTotalCriptomonedas)
        usuario.dineroEnCuenta = usuario.dineroEnCuenta.minus(dineroTotal)
        usuario.dineroEnCuenta = usuario.dineroEnCuenta.plus(cashback)

        return valorTotalCriptomonedas
    }

    private fun agregarSaldo(usuario: Usuario, saldo: Double) {
            usuario.dineroEnCuenta.plus(saldo)
    }

    private fun calcularDineroTotal(dineroACambiar: Double, exchange: Exchange): Double {
        val comision = dineroACambiar.times(
            when (exchange) {
                Criptomas -> Criptomas.calcularComision()
                Criptodia -> Criptodia.calcularComision()
                Carrecripto -> Carrecripto.calcularComision()
                else -> Criptomas.calcularComision()

            }
        )
        return dineroACambiar.plus(comision)
    }

    private fun otorgarCashback(usuario: Usuario): Double {
        return when (Period.between(usuario.fechaAlta, LocalDate.now()).toTotalMonths()) {
            in 0..3 -> 0.05
            in 4..12 -> 0.03
            else -> 0.0
        }
    }

    private fun setUpRecyclerView(usuario: Usuario) {
        val selectCompraClickLister = { compra: Compra ->
            Toast.makeText(this, "Tocaste la compra de código ${compra.codigoCompra}", Toast.LENGTH_LONG).show()
        }
        recyclerViewCompras = findViewById(R.id.rvCompras)
        recyclerViewCompras.adapter = ComprasAdapter(CompraRepositorio.obtenerListaDeComprasPorUsuario(usuario.nickname), selectCompraClickLister)
        recyclerViewCompras.layoutManager = LinearLayoutManager(this)
    }
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
