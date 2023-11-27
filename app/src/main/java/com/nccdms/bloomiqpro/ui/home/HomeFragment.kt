package com.nccdms.bloomiqpro.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nccdms.bloomiqpro.R
import com.nccdms.bloomiqpro.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewmodel:HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = getString(R.string.app_name)
            navigationIcon = null
            inflateMenu(R.menu.main_menu)
            setOnMenuItemClickListener{menuItem ->
                when(menuItem.itemId){
                    R.id.logout -> {
                        viewmodel.clearSession()
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                    }
                }
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}