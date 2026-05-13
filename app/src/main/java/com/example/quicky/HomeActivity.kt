package com.example.quicky

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter

    // ViewModels
    private val cartViewModel: CartViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Adapter with Add to Cart callback
        adapter = ProductAdapter(emptyList()) { product ->
            cartViewModel.addToCart(product)   // ✅ Add product to shared CartManager via CartViewModel
        }
        recyclerView.adapter = adapter

        // Observe product list from ProductViewModel
        productViewModel.products.observe(this) { products ->
            adapter.updateProducts(products)
        }

        // Navigate to CartActivity
        findViewById<Button>(R.id.btnGoToCart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}
