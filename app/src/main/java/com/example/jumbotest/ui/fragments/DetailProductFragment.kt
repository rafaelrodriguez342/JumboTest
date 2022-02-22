package com.example.jumbotest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.jumbotest.R
import com.example.jumbotest.business.viewmodels.ProductListViewModel
import com.example.jumbotest.databinding.FragmentDetailProductBinding
import com.example.jumbotest.ui.image.ImageHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Fragment to display a specific product data.
 */
@AndroidEntryPoint
class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val productListViewModel: ProductListViewModel by activityViewModels()
    private val binding get() = _binding!!
    @Inject
    lateinit var imageHandler: ImageHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productListViewModel.selectedProduct().observe(viewLifecycleOwner) {
            binding.detailName.text = it.name
            binding.detailPrice.text =
                getString(R.string.detail_price_template, it.price, it.currency)
            imageHandler.loadPicture(binding.detailImage, it.imageUrl)

            binding.detailAvailable.text =
                if (it.available) getString(R.string.available_label) else getString(R.string.not_available_label)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
