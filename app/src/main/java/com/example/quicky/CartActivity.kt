package com.example.quicky

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var totalText: TextView
    private lateinit var placeOrderBtn: Button

    private val cartViewModel: CartViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.recyclerCart)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ Pass remove callback to adapter
        adapter = CartAdapter(emptyList()) { product ->
            cartViewModel.removeFromCart(product)
        }
        recyclerView.adapter = adapter

        totalText = findViewById(R.id.txtTotal)
        placeOrderBtn = findViewById(R.id.btnPlaceOrder)

        // Refresh cart when opening this screen
        cartViewModel.refreshCart()

        // Observe cart items
        cartViewModel.cartItems.observe(this) { items ->
            adapter.updateCart(items)
            val total = items.sumOf { it.product.price.toInt() * it.quantity }
            totalText.text = "Total: ₹$total"
        }

        // Handle place order
        placeOrderBtn.setOnClickListener {
            if (cartViewModel.cartItems.value.isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.cart_empty), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.order_success), Toast.LENGTH_SHORT).show()
                cartViewModel.clearCart()
                finish()
            }
        }
    }
}
