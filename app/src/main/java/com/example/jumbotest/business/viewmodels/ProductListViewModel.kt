package com.example.jumbotest.business.viewmodels

import androidx.lifecycle.*
import com.example.jumbotest.business.model.Order
import com.example.jumbotest.business.model.Product
import com.example.jumbotest.data.repositories.CartRepository
import com.example.jumbotest.data.repositories.ProductDatabaseRepository
import com.example.jumbotest.data.repositories.ProductRepository
import com.example.jumbotest.data.util.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val productDatabaseRepository: ProductDatabaseRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val productList: MutableLiveData<List<Product>> = MutableLiveData()
    private val errorMessage: MutableLiveData<String> = MutableLiveData()
    private val selectedProduct: MutableLiveData<Product> = MutableLiveData()
    private val displayLoader: MutableLiveData<Boolean> = MutableLiveData()
    private val cart: MutableLiveData<Map<String, Order>> = MutableLiveData()
    private val navigation: MutableLiveData<NavigationScreens> = MutableLiveData()
    private var cartOrders: MutableMap<String, Order> = mutableMapOf()
    val cartCount: LiveData<Int> = Transformations.map(cart) { orders ->
        var count = 0
        orders.forEach {
            count += it.value.quantity
        }
        count
    }

    val cartProductList: LiveData<List<Product>> = Transformations.map(cart) { orders ->
        orders.map { it.value.product }
    }

    fun bringProducts() {
        viewModelScope.launch(coroutineDispatcher) {
            displayLoader.postValue(true)
            if (networkUtils.isNetworkAvailable()) {
                loadProductsFromRepository(productRepository)?.let {
                    productDatabaseRepository.saveProducts(it)
                }
            } else {
                loadProductsFromRepository(productDatabaseRepository)
            }
            displayLoader.postValue(false)
        }
    }

    fun onAppPaused() {
        saveCurrentCart()
    }

    fun addProductToCart(product: Product) {
        val currentOrder = cartOrders.getOrPut(product.id) {
            Order(product, 0)
        }

        currentOrder.quantity = currentOrder.quantity.inc()
        notifyCartStateChange()
    }

    fun removeProductFromCart(product: Product) {
        cartOrders[product.id]?.let {
            if (it.quantity <= 1) {
                cartOrders.remove(product.id)
            } else {
                it.quantity = it.quantity.dec()
            }
        }
        notifyCartStateChange()
    }

    fun selectProduct(product: Product) {
        selectedProduct.value = product
        navigation.value = NavigationScreens.DETAIL_VIEW
    }

    fun onCartPreviewClicked() {
        navigation.value = NavigationScreens.CART_VIEW
    }

    fun getCart(): LiveData<Map<String, Order>> = cart

    fun productList(): LiveData<List<Product>> = productList

    fun selectedProduct(): LiveData<Product> = selectedProduct

    fun navigation(): LiveData<NavigationScreens> = navigation

    fun displayLoader(): LiveData<Boolean> = displayLoader

    fun errorMessage(): LiveData<String> = errorMessage

    private fun notifyCartStateChange() {
        cart.postValue(cartOrders)
    }

    private fun saveCurrentCart() {
        viewModelScope.launch(coroutineDispatcher) {
            cartRepository.saveCart(cartOrders)
        }
    }

    private fun loadCurrentCart(productList: List<Product>) {
        cartOrders = cartRepository.getCart(productList).toMutableMap()
        notifyCartStateChange()
    }

    private fun loadProductsFromRepository(selectedProductRepository: ProductRepository): List<Product>? {
        val response = selectedProductRepository.getProducts()
        return if (response.isSuccess) {
            errorMessage.postValue(null)
            response.payload?.let {
                productList.postValue(it)
                loadCurrentCart(it)
                it
            }
        } else {
            errorMessage.postValue(response.errorMessage)
            null
        }
    }
}

enum class NavigationScreens {
    CART_VIEW, DETAIL_VIEW
}
