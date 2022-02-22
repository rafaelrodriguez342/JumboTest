package com.example.jumbotest.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jumbotest.R
import com.example.jumbotest.business.model.Order
import com.example.jumbotest.business.model.Product
import com.example.jumbotest.ui.image.ImageHandler

class ProductListAdapter(
    private val productList: List<Product>,
    private var cart: Map<String, Order>?,
    private val productListConfiguration: ProductListConfiguration,
    private val imageHandler: ImageHandler,
    private val productItemListener: ProductItemListener?
) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val nameTextView: TextView = view.findViewById(R.id.productName)
        private val priceTextView: TextView = view.findViewById(R.id.productPrice)
        private val image: ImageView = view.findViewById(R.id.productImage)
        private val removeButton: ImageView = view.findViewById(R.id.decreaseProductInCartButton)
        private val addButton: ImageView = view.findViewById(R.id.increaseProductInCart)
        private val quantityLabelTextView: TextView = view.findViewById(R.id.quantityLabel)
        private val currencyLabelTextView: TextView = view.findViewById(R.id.productCurrency)

        fun bindProduct(product: Product) {
            nameTextView.text = product.name
            priceTextView.text = product.price
            currencyLabelTextView.text = product.currency
            imageHandler.loadPicture(image, product.imageUrl)
            quantityLabelTextView.text = ""

            cart?.get(product.id)?.let {
                quantityLabelTextView.visibility = View.VISIBLE
                if (productListConfiguration.displayQuantityButtonsForCartItems) {
                    removeButton.visibility = View.VISIBLE
                    addButton.visibility = View.VISIBLE
                } else {
                    removeButton.visibility = View.INVISIBLE
                    addButton.visibility = View.INVISIBLE
                }
                quantityLabelTextView.text = it.quantity.toString()
            } ?: run {
                removeButton.visibility = View.GONE
                quantityLabelTextView.visibility = View.GONE
            }

            removeButton.setOnClickListener {
                productItemListener?.decreaseProduct(product)
            }

            addButton.setOnClickListener {
                productItemListener?.increaseProduct(product)
            }

            view.setOnClickListener {
                productItemListener?.selectProduct(product)
            }
        }
    }

    fun updateCartData(cart: Map<String, Order>?) {
        this.cart = cart
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productList[position]
        holder.bindProduct(item)
    }

    override fun getItemCount(): Int = productList.size

}

data class ProductListConfiguration(val displayQuantityButtonsForCartItems: Boolean)

interface ProductItemListener {

    fun selectProduct(product: Product)

    fun decreaseProduct(product: Product)

    fun increaseProduct(product: Product)
}
