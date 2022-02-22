package com.example.jumbotest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jumbotest.business.model.Product
import com.example.jumbotest.business.viewmodels.ProductListViewModel
import com.example.jumbotest.databinding.FragmentProductListBinding
import com.example.jumbotest.ui.adapters.ProductItemListener
import com.example.jumbotest.ui.adapters.ProductListAdapter
import com.example.jumbotest.ui.adapters.ProductListConfiguration
import com.example.jumbotest.ui.image.ImageHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Fragment displaying the product list.
 */
@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val productListViewModel: ProductListViewModel by activityViewModels()
    private val binding get() = _binding!!
    private var adapter: ProductListAdapter? = null
    @Inject
    lateinit var imageHandler: ImageHandler


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.productList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.productList.layoutManager = LinearLayoutManager(requireContext())
        productListViewModel.getCart().observe(viewLifecycleOwner) {
            adapter?.updateCartData(it)
        }

        productListViewModel.productList().observe(viewLifecycleOwner) {
            adapter = ProductListAdapter(it,
                productListViewModel.getCart().value,
                ProductListConfiguration(true),
                imageHandler,
                object : ProductItemListener {
                    override fun selectProduct(product: Product) {
                        productListViewModel.selectProduct(product)
                    }

                    override fun decreaseProduct(product: Product) {
                        productListViewModel.removeProductFromCart(product)
                    }

                    override fun increaseProduct(product: Product) {
                        productListViewModel.addProductToCart(product)
                    }

                })

            binding.productList.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
