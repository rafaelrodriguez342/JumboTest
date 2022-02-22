package com.example.jumbotest.business.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.jumbotest.business.model.Order
import com.example.jumbotest.business.model.Product
import com.example.jumbotest.data.repositories.CartRepository
import com.example.jumbotest.data.repositories.ProductDatabaseRepository
import com.example.jumbotest.data.repositories.ProductRepository
import com.example.jumbotest.data.repositories.RepositoryResponse
import com.example.jumbotest.data.util.NetworkUtils
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ProductListViewModelTest {

    @Rule
    @JvmField
    var ruleInstantExecution: TestRule = InstantTaskExecutorRule()

    private lateinit var productListViewModel: ProductListViewModel

    @MockK
    private lateinit var productRepository: ProductRepository

    @MockK
    private lateinit var productDatabaseRepository: ProductDatabaseRepository

    @MockK
    private lateinit var cartRepository: CartRepository

    @MockK
    private lateinit var networkUtils: NetworkUtils

    @Before
    fun setUp() {

        MockKAnnotations.init(this)
        productListViewModel = ProductListViewModel(
            productRepository,
            productDatabaseRepository,
            Dispatchers.Unconfined,
            cartRepository,
            networkUtils
        )
    }

    private fun mockProductList(productList: List<Product>, productRepository: ProductRepository) {
        val testResponse = RepositoryResponse(
            true,
            productList,
            null
        )

        every { productRepository.getProducts() } returns testResponse
    }

    @Test
    fun `get product list from network when online`() {
        val productList = listOf(Product("123456", "chocolate", "testUrl", "234", "EUR", true))
        mockProductList(productList, productRepository)

        every { cartRepository.getCart(productList) } returns mutableMapOf()

        every { networkUtils.isNetworkAvailable() } returns true

        productListViewModel.bringProducts()

        verify { productRepository.getProducts() }

        verify { cartRepository.getCart(productList) }

        verify { productDatabaseRepository.saveProducts(productList) }

        assertEquals(productList, productListViewModel.productList().value)

    }

    @Test
    fun `get product list from database when offline`() {
        val productList = listOf(Product("123456", "chocolate", "testUrl", "234", "EUR", true))
        mockProductList(productList, productDatabaseRepository)

        every { networkUtils.isNetworkAvailable() } returns false

        every { cartRepository.getCart(productList) } returns mutableMapOf()

        productListViewModel.bringProducts()

        verify { productDatabaseRepository.getProducts() }

        verify { productRepository wasNot Called }

        verify { cartRepository.getCart(productList) }

        assertEquals(productList, productListViewModel.productList().value)
    }

    @Test
    fun `save cart when app paused`() {
        productListViewModel.onAppPaused()
        verify { cartRepository.saveCart(any()) }
    }

    @Test
    fun `send error message when response has error`() {
        val errorMessage = "test message"
        val testResponse = RepositoryResponse<List<Product>>(
            false,
            null,
            errorMessage
        )

        every { productRepository.getProducts() } returns testResponse

        every { networkUtils.isNetworkAvailable() } returns true

        productListViewModel.bringProducts()

        assertEquals(errorMessage, productListViewModel.errorMessage().value)
    }

    @Test
    fun `update cart and count when adding a product to the cart`() {
        val product = Product("123456", "chocolate", "testUrl", "234", "EUR", true)
        val productList = listOf(product)
        mockProductList(productList, productRepository)
        every { cartRepository.getCart(productList) } returns mutableMapOf()

        every { networkUtils.isNetworkAvailable() } returns true

        productListViewModel.bringProducts()
        productListViewModel.addProductToCart(product)

        // this is done because mediator livedata does not receive updates if it does not have active observers.
        productListViewModel.cartCount.observeForever { }

        assertEquals(1, productListViewModel.cartCount.value)
        assertEquals(mapOf(product.id to Order(product, 1)), productListViewModel.getCart().value)

        productListViewModel.addProductToCart(product)
        assertEquals(2, productListViewModel.cartCount.value)
        assertEquals(mapOf(product.id to Order(product, 2)), productListViewModel.getCart().value)
    }

    @Test
    fun `update cart and count when removing elements in the cart`() {
        val product = Product("123456", "chocolate", "testUrl", "234", "EUR", true)
        val productList = listOf(product)
        mockProductList(productList, productRepository)

        every { cartRepository.getCart(productList) } returns mapOf(product.id to Order(product, 2))

        every { networkUtils.isNetworkAvailable() } returns true

        productListViewModel.bringProducts()

        productListViewModel.removeProductFromCart(product)

        // this is done because mediator livedata does not receive updates if it does not have active observers.
        productListViewModel.cartCount.observeForever { }

        assertEquals(1, productListViewModel.cartCount.value)
        assertEquals(mapOf(product.id to Order(product, 1)), productListViewModel.getCart().value)

        productListViewModel.removeProductFromCart(product)

        assertEquals(0, productListViewModel.cartCount.value)
        assertEquals(mapOf<String, Order>(), productListViewModel.getCart().value)
    }

    @Test
    fun `update selected product and navigate to detail when selecting an item in list`() {
        val product = Product("123456", "chocolate", "testUrl", "234", "EUR", true)
        val productList = listOf(product)
        mockProductList(productList, productRepository)

        every { cartRepository.getCart(productList) } returns mapOf(product.id to Order(product, 2))

        every { networkUtils.isNetworkAvailable() } returns true

        productListViewModel.bringProducts()

        productListViewModel.selectProduct(product)

        assertEquals(product, productListViewModel.selectedProduct().value)
        assertEquals(NavigationScreens.DETAIL_VIEW, productListViewModel.navigation().value)
    }

    @Test
    fun `go to cart screen when clicking preview cart`() {
        productListViewModel.onCartPreviewClicked()
        assertEquals(NavigationScreens.CART_VIEW, productListViewModel.navigation().value)
    }

}
