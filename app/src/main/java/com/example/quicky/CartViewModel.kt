package com.example.quicky

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    fun addToCart(product: Product) {
        CartManager.addToCart(product)
        _cartItems.value = CartManager.getCartItems()
    }

    fun removeFromCart(product: Product) {
        CartManager.removeFromCart(product)
        _cartItems.value = CartManager.getCartItems()
    }

    fun clearCart() {
        CartManager.clearCart()
        _cartItems.value = CartManager.getCartItems()
    }

    fun refreshCart() {
        _cartItems.value = CartManager.getCartItems()
    }

    fun getTotalPrice(): Int {
        return CartManager.getCartItems().sumOf { it.product.price.toInt() * it.quantity }
    }
}
