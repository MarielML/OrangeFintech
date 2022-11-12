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
import com.example.orangefintech.entidades.Compra
import com.example.orangefintech.entidades.Criptodia
import com.example.orangefintech.entidades.Criptomas
import com.example.orangefintech.entidades.Criptomonedas
import com.example.orangefintech.repositorios.CompraRepositorio
import com.example.orangefintech.repositorios.UsuarioRepositorio
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.LocalTime

class PantallaPrincipalActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private lateinit var spinner: Spinner
    private lateinit var inicio: Button
    private lateinit var cuenta: TextView
    private lateinit var comprar: Button
    private lateinit var monto: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantallaprincipal)

        val bundle = intent.extras
        val codigoDeCuenta = bundle?.getInt("codigo")

        cuenta = findViewById<TextView>(R.id.tvCuenta)
        cuenta.text = codigoDeCuenta?.let { UsuarioRepositorio.obtenerPorCodigo(it).toString() }

        comprar = findViewById(R.id.buttonComprar)
        comprar.setOnClickListener {
            monto = findViewById(R.id.etMonto)
            val usuarioNickname = bundle?.getString("nickname")
            val nuevoCodigoCompra: Int = CompraRepositorio.compra.last().codigoCompra.plus(1)
            val fechaAhora = LocalDate.now()
            val horaAhora = LocalTime.now()
            val criptomonedaTipo = when(spinner.selectedItem.toString()) {
                "Criptomas" -> Criptomonedas.CRIPTODIA
                "Criptodia" -> Criptomonedas.CRIPTODIA
                "Carrecripto" -> Criptomonedas.CARRECRIPTO
                else -> Toast.makeText(this, "Por favor selecciona un exchange", Toast.LENGTH_SHORT).show()
            }
            val valorTotalCriptomonedas = 0.0
            val dineroACambiar = monto.text.toString().toDouble()
            val comision = when(criptomonedaTipo){
                Criptomonedas.CRIPTOMAS -> "2%"
                Criptomonedas.CRIPTODIA -> if(Criptodia.calcularComision() == 0.01) "1%" else "3%"
                Criptomonedas.CARRECRIPTO -> if (Criptomas.calcularComision() == 0.03) "(3%)" else "(0.75%)"
                else -> ""
            }
            val nuevaCompra = usuarioNickname?.let { it1 ->
                Compra(
                    it1, nuevoCodigoCompra, fechaAhora, horaAhora,
                    criptomonedaTipo as Criptomonedas,
                    valorTotalCriptomonedas, dineroACambiar, comision)
            }
            if (nuevaCompra != null) {
                CompraRepositorio.agregar(nuevaCompra)
            }

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


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")

    }
