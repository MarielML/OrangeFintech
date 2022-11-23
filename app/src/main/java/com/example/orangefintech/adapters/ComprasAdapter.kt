package com.example.orangefintech.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.orangefintech.databinding.ItemCompraBinding
import com.example.orangefintech.entidades.Compra

class ComprasAdapter (private val compras: List<Compra>, private val selectClientClickLister: (Compra) -> Unit) : RecyclerView.Adapter<ComprasAdapter.ComprasViewHolder>(){

    class ComprasViewHolder (val binding: ItemCompraBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComprasViewHolder {
        val compraBinding = ItemCompraBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ComprasViewHolder(compraBinding)
    }

    override fun onBindViewHolder(holder: ComprasViewHolder, position: Int) {
        val compra = compras[position]

        holder.binding.txtCodigo.text = compra.codigoCompra.toString()
        holder.binding.txtFecha.text = compra.fechaCompra.toString()
        holder.binding.txtHora.text = compra.horaCompra.toString()
        holder.binding.txtCriptomoneda.text = compra.criptomoneda.toString()
        holder.binding.txtValorAdquirido.text = compra.valorAdquirido.toString()
        holder.binding.txtValorPagado.text = compra.valorPagado.toString()
        holder.binding.txtComision.text = compra.comision
        

        holder.binding.comprasCard.setOnClickListener {
            selectClientClickLister(compra)
        }
    }

    override fun getItemCount(): Int {
        return compras.size
    }
}
