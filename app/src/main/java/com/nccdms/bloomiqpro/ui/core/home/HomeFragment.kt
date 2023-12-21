package com.nccdms.bloomiqpro.ui.core.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.databinding.FragmentHomeBinding
import com.nccdms.bloomiqpro.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: HomeViewModel by viewModels()
    private lateinit var flowerAdapter: AdapterFlower
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupObserve()
        setupSearch()
        setupQuote()

        lifecycleScope.launch {
            viewmodel.getFlowers()
        }
    }

    private fun setupQuote() {
        viewmodel.currentQuote.observe(viewLifecycleOwner) { quote ->
            binding.tvQuote.text = quote.text
            binding.imageView.setImageResource(quote.image)
        }
        // Trigger to set a random quote
        viewmodel.setRandomQuote()
    }

    private fun setupSearch() {
       binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
           override fun onQueryTextSubmit(query: String?): Boolean {
               // Handle query submission if needed
               return true
           }

           override fun onQueryTextChange(newText: String?): Boolean {
               if (newText.isNullOrBlank()) {
                   // If the query is empty, reset the adapter with the original list
                   flowerAdapter.submitList(viewmodel.flowers.value)
               } else {
                   // Filter the adapter based on the search query
                   val filteredList = viewmodel.flowers.value
                       ?.filter { it.flowerName?.contains(newText, true) == true }
                   flowerAdapter.submitList(filteredList)
               }
               return true
           }
       })
    }

    private fun setupObserve() {
        viewmodel.flowersResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    // Update the adapter with the list of flowers
                    showLoading(false)
                    flowerAdapter.submitList(result.data)
                }
                is ApiResult.Error -> {
                    // Handle error
                    showLoading(false)
                    showToast("Error: ${result.message}")
                }
                is ApiResult.Loading -> {
                    showLoading(true)
                }
            }
        }

        // Trigger the API call when the view is created
        viewmodel.getFlowers()
    }

    private fun setupAdapter() {
        flowerAdapter = AdapterFlower { flower ->
            // Handle item click, for example, navigate to detail fragment
            // Use the flower data to pass necessary information
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(flower.flowerName?.lowercase() ?: "")
            findNavController().navigate(action)
        }

        binding.listView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = flowerAdapter
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