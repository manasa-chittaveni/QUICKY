package com.example.quicky

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onRemove: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtCartProductName)
        val price: TextView = view.findViewById(R.id.txtCartPrice)
        val quantity: TextView = view.findViewById(R.id.txtCartQuantity)
        val image: ImageView = view.findViewById(R.id.imgCartProduct)
        val removeBtn: Button = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        holder.name.text = item.product.name
        holder.price.text = holder.itemView.context.getString(
            R.string.price_format, item.product.price.toInt()
        )
        holder.quantity.text = holder.itemView.context.getString(
            R.string.quantity_format, item.quantity
        )
        holder.image.setImageResource(item.product.imageRes)

        // ✅ Delegate removal to ViewModel via callback
        holder.removeBtn.setOnClickListener {
            onRemove(item.product)
        }
    }

    override fun getItemCount(): Int = cartItems.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateCart(newItems: List<CartItem>) {
        cartItems = newItems
        notifyDataSetChanged()
    }
}
