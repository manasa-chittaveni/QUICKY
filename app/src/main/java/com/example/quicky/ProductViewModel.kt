package com.example.quicky

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    init {
        loadProducts()
    }

    private fun loadProducts() {
        _products.value = listOf(
            Product("Apples", "Fruits", 120.0, R.drawable.apple),
            Product("Bananas", "Fruits", 50.0, R.drawable.banana),
            Product("Tomatoes", "Vegetables", 40.0, R.drawable.tomato),
            Product("Potatoes", "Vegetables", 30.0, R.drawable.potato),
            Product("Milk", "Dairy", 60.0, R.drawable.milk),
            Product("Cheese", "Dairy", 150.0, R.drawable.cheese),
            Product("Rice", "Grains", 80.0, R.drawable.rice),
            Product("Wheat Flour", "Grains", 55.0, R.drawable.wheet),
            Product("Orange Juice", "Beverages", 90.0, R.drawable.orange),
            Product("Tea Pack", "Beverages", 120.0, R.drawable.greentea),
            Product("Biscuits", "Snacks", 40.0, R.drawable.oreo),
            Product("Chips", "Snacks", 30.0, R.drawable.bingo),
            Product("Sugar", "Essentials", 45.0, R.drawable.sugar),
            Product("Salt", "Essentials", 20.0, R.drawable.salt),
            Product("Cooking Oil", "Essentials", 160.0, R.drawable.oil)
        )
    }
}
