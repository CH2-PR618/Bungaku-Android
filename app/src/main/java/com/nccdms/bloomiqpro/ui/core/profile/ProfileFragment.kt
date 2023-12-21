package com.nccdms.bloomiqpro.ui.core.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.databinding.FragmentProfileBinding
import com.nccdms.bloomiqpro.ui.auth.AuthActivity
import com.nccdms.bloomiqpro.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve()
        setupObserveLogout()
        setupData()
        setupAction()
    }

    private fun setupObserveLogout() {
        viewmodel.logoutResult.observe(viewLifecycleOwner){result ->
            when (result) {
                is ApiResult.Success -> {
                    showLoading(false)
                    showToast("Logout successful")
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    startActivity(intent)
                }
                is ApiResult.Error -> {
                    showLoading(false)
                    showToast("Logout failed: ${result.message}")
                }
                is ApiResult.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            cvLogout.setOnClickListener {
                viewmodel.logout()
            }
            cvChangeName.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToNewNameFragment())
            }
            cvChangeEmail.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToNewEmailFragment())
            }
            cvChangePassword.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToNewPasswordFragment())
            }
        }
    }

    private fun setupData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.getUserProfile()
        }
    }

    private fun setupObserve() {
        viewmodel.userProfileResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    // Handle success, update UI with user profile information
                    showLoading(false)
                    val user = result.data
                    binding.tvUsername.text = user.name
                    binding.tvEmail.text = user.email
                    // You can update other UI components accordingly
                }
                is ApiResult.Error -> {
                    // Handle error, show an error message or retry option
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