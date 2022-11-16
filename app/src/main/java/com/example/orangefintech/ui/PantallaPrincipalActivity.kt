package com.example.orangefintech.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.orangefintech.R
import com.example.orangefintech.databinding.ActivityMainBinding
import com.example.orangefintech.entidades.*
import com.example.orangefintech.excepciones.SaldoInsuficiente
import com.example.orangefintech.repositorios.CompraRepositorio
import com.example.orangefintech.repositorios.UsuarioRepositorio
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text
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
    private lateinit var agregarSaldo: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantallaprincipal)

        val bundle = intent.extras
        val codigoDeCuenta = bundle?.getInt("codigo")
        val nickname: String? = bundle?.getString("nickname")
        val password: String? = bundle?.getString("password")
        val usuario = if(nickname != null && password != null) UsuarioRepositorio.iniciar(nickname, password) else null
        val etSaldo = findViewById<EditText>(R.id.etSaldo)
        val saldo = etSaldo.text.toString().toDouble()
        val agregar = findViewById<FloatingActionButton>(R.id.floatingActionButtonSaldo)

        cuenta = findViewById<TextView>(R.id.tvCuenta)
        cuenta.text = codigoDeCuenta?.let { UsuarioRepositorio.obtenerPorCodigo(it).toString() }

        comprar = findViewById(R.id.buttonComprar)
        comprar.setOnClickListener {
            monto = findViewById(R.id.etMonto)
            val usuarioNickname: String? = usuario?.nickname
            val nuevoCodigoCompra: Int = CompraRepositorio.compra.last().codigoCompra.plus(1)
            val fechaAhora = LocalDate.now()
            val horaAhora = LocalTime.now()
            val criptomonedaTipo = when(spinner.selectedItem.toString()) {
                "Criptomas" -> Criptomonedas.CRIPTODIA
                "Criptodia" -> Criptomonedas.CRIPTODIA
                "Carrecripto" -> Criptomonedas.CARRECRIPTO
                else -> Toast.makeText(this, "Por favor selecciona un exchange", Toast.LENGTH_SHORT).show()
            }
            val dineroACambiar = monto.text.toString().toDouble()
            val dineroTotal = calcularDineroTotal(dineroACambiar, criptomonedaTipo as Exchange)
            try {
                if (usuario?.checkDineroACambiar(dineroTotal) == true) {
                    val comision = when(criptomonedaTipo) {
                        Criptomonedas.CRIPTOMAS -> "2%"
                        Criptomonedas.CRIPTODIA -> if (Criptodia.calcularComision() == 0.01) "1%" else "3%"
                        Criptomonedas.CARRECRIPTO -> if (Criptomas.calcularComision() == 0.03) "(3%)" else "(0.75%)"
                        else -> ""
                    }
                    val cashback = if(usuario != null ) dineroTotal.times(otorgarCashback(usuario)) else 0.0
                    val valorTotalCriptomonedas = editarUsuarioAlComprar(monto.text.toString().toDouble(), dineroTotal, cashback, usuario)
                    val nuevaCompra = if(usuarioNickname != null) Compra(usuarioNickname, nuevoCodigoCompra, fechaAhora, horaAhora, criptomonedaTipo as Criptomonedas,
                        valorTotalCriptomonedas, dineroACambiar, comision) else null
                    if (nuevaCompra != null) {
                        CompraRepositorio.agregar(nuevaCompra)
                    }
                    var mensaje = """
                         COMPRA REALIZADA
                         Usted compró ${valorTotalCriptomonedas} Criptomonedas de ${criptomonedaTipo.toString()}
                         Se cobró una comisión de ${'$'} ${comision}
                    """.trimIndent()

                    mensaje += if (cashback != 0.0) """
                        Se otorgó un cashback de $cashback
                        Muchas gracias por la compra.
                    """.trimIndent()
                    else "Muchas gracias por la compra."
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                }
                val codigoDeLaCuenta: Int? = usuario?.codigoCuenta
                if (codigoDeLaCuenta != null) {
                    UsuarioRepositorio.editarPorCodigo(codigoDeLaCuenta, usuario)
                }
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

        spinner = findViewById(R.id.spinnerExchanges)
        ArrayAdapter.createFromResource(this, R.array.exchange_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }

    fun editarUsuarioAlComprar(dineroACambiar: Double, dineroTotal: Double, cashback: Double, usuario: Usuario?): Double {
        val VALOR_CRIPTOMONEDA = 1.0
        val VALOR_DINERO = 50.0

        val valorTotalCriptomonedas = (dineroACambiar.times(VALOR_CRIPTOMONEDA)).div(VALOR_DINERO)
        usuario?.criptomonedasEnCuenta = usuario?.criptomonedasEnCuenta?.plus(valorTotalCriptomonedas)!!
        usuario?.dineroEnCuenta = usuario?.dineroEnCuenta?.minus(dineroTotal)!!
        usuario?.dineroEnCuenta = usuario?.dineroEnCuenta?.plus(cashback)!!

        return valorTotalCriptomonedas
    }

    fun agregarSaldo(usuario: Usuario?, saldo: Double) {
            usuario?.dineroEnCuenta?.plus(saldo)!!
    }

    fun calcularDineroTotal(dineroACambiar: Double, exchange: Exchange): Double {
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

    fun otorgarCashback(usuario: Usuario): Double {
        val periodo = Period.between(usuario.fechaAlta, LocalDate.now()).toTotalMonths()
        return when (periodo) {
            in 0..3 -> 0.05
            in 4..12 -> 0.03
            else -> 0.0
        }
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
