package com.nccdms.bloomiqpro.ui.core.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.databinding.FragmentDetailBinding
import com.nccdms.bloomiqpro.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve()

        // Get flower name from arguments
        val flowerName = DetailFragmentArgs.fromBundle(requireArguments()).flowerName.lowercase()

        lifecycleScope.launch {
            viewmodel.getFlowerDetails(flowerName)
        }
    }

    private fun setupObserve() {
        viewmodel.flowerDetailResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    // Update UI with flower details
                    val flowerDetail = result.data
                    binding.apply {
                        showLoading(false)
                        // Set text views with flower details
                        ivflower.load(flowerDetail.flowerImage)
                        tvTitleInput.text = flowerDetail.flowerName.orEmpty()
                        tvAsalInput.text = flowerDetail.flowerOrigin.orEmpty()
                        tvBungaInput.text = flowerDetail.flowerPlace.orEmpty()
                        tvMusimInput.text = flowerDetail.flowerSeason.orEmpty()
                        tvScinceInput.text = flowerDetail.flowerScientific.orEmpty()
                        tvDescInput.text = flowerDetail.flowerDescription.orEmpty()
                    }
                }
                is ApiResult.Error -> {
                    showLoading(false)
                    showToast("Error: ${result.message}")
                }
                is ApiResult.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.isVisible = state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}