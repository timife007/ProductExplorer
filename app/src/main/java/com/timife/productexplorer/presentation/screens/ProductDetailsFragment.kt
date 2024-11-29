package com.timife.productexplorer.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.timife.productexplorer.R
import com.timife.productexplorer.databinding.FragmentProductDetailsBinding
import com.timife.productexplorer.presentation.ui_states.DetailUiState
import com.timife.productexplorer.presentation.utils.Utils
import com.timife.productexplorer.presentation.viewmodels.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            viewModel.detailUiState.collectLatest { state ->
                when (state) {
                    is DetailUiState.Loading -> {
                        // Show loading state
                        binding.detailsProgress.visibility = View.VISIBLE
                    }

                    is DetailUiState.Success -> {
                        binding.detailsProgress.visibility = View.GONE
                        binding.apply {
                            Utils.loadImage(
                                requireContext(),
                                productDetailImage,
                                state.data.image,
                                imageProgress
                            )
                            title.text = state.data.title
                            description.text = state.data.description
                            rating.text =
                                String.format(Locale.getDefault(),
                                    getString(R.string._1f), state.data.rating)
                            reviews.text = getString(R.string._no_of_reviews, state.data.review)
                        }
                    }

                    is DetailUiState.Error -> {
                        // Show error state
                        Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
        return binding.root
    }
}