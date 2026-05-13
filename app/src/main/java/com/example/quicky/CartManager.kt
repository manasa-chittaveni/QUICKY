package com.example.quicky

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun addToCart(product: Product) {
        val existing = cartItems.find { it.product.name == product.name }
        if (existing != null) {
            existing.quantity += 1
        } else {
            cartItems.add(CartItem(product, 1))
        }
    }

    fun removeFromCart(product: Product) {
        val existing = cartItems.find { it.product.name == product.name }
        if (existing != null) {
            cartItems.remove(existing)   // ✅ remove only the clicked item
        }
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getCartItems(): List<CartItem> = cartItems.toList()
}
