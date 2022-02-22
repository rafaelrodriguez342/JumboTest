package com.example.jumbotest.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.jumbotest.R
import com.example.jumbotest.business.viewmodels.NavigationScreens
import com.example.jumbotest.business.viewmodels.ProductListViewModel
import com.example.jumbotest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity in charge of displaying products related fragments.
 */
@AndroidEntryPoint
class ProductsActivity : AppCompatActivity() {

    private val productListViewModel: ProductListViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        productListViewModel.bringProducts()
        productListViewModel.cartCount.observe(this) {
            binding.cartPreview.cartCounter.text = it.toString()
        }

        productListViewModel.navigation().observe(this) {
            when (it) {
                NavigationScreens.CART_VIEW -> navigateTo(R.id.carProductsFragment)
                NavigationScreens.DETAIL_VIEW -> navigateTo(R.id.detailProductFragment)
            }
        }

        productListViewModel.displayLoader().observe(this) {
            if (it) {
                binding.loader.root.visibility = View.VISIBLE
            } else {
                binding.loader.root.visibility = View.GONE
            }
        }

        productListViewModel.errorMessage().observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        binding.cartPreview.root.setOnClickListener {
            productListViewModel.onCartPreviewClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        productListViewModel.onAppPaused()
    }

    private fun navigateTo(@IdRes fragmentId: Int) {
        val navBuilder = NavOptions.Builder()
        navBuilder.setEnterAnim(android.R.anim.slide_in_left)
            .setExitAnim(android.R.anim.slide_out_right)
            .setPopEnterAnim(android.R.anim.slide_in_left)
            .setPopExitAnim(android.R.anim.slide_out_right)

        findNavController(R.id.navHostFragment).navigate(fragmentId, null, navBuilder.build())
    }
}
