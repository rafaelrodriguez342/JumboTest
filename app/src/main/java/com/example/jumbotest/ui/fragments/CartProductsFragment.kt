package com.example.jumbotest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jumbotest.business.viewmodels.ProductListViewModel
import com.example.jumbotest.databinding.FragmentCartProductsBinding
import com.example.jumbotest.ui.adapters.ProductListAdapter
import com.example.jumbotest.ui.adapters.ProductListConfiguration
import com.example.jumbotest.ui.image.ImageHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Fragment to display user cart.
 */
@AndroidEntryPoint
class CartProductsFragment : Fragment() {

    private var _binding: FragmentCartProductsBinding? = null
    private val binding get() = _binding!!
    private val productListViewModel: ProductListViewModel by activityViewModels()
    private var adapter: ProductListAdapter? = null
    @Inject
    lateinit var imageHandler: ImageHandler


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCartProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardList.layoutManager = LinearLayoutManager(requireContext())
        binding.cardList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        productListViewModel.cartProductList.observe(viewLifecycleOwner) {
            adapter = ProductListAdapter(
                it, productListViewModel.getCart().value, ProductListConfiguration(false),
                imageHandler, null
            )
            binding.cardList.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
