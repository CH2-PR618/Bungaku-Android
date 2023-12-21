package com.nccdms.bloomiqpro.ui.core.profile.change

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nccdms.bloomiqpro.R
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.databinding.FragmentNewEmailBinding
import com.nccdms.bloomiqpro.databinding.FragmentProfileBinding
import com.nccdms.bloomiqpro.ui.core.profile.ProfileViewModel
import com.nccdms.bloomiqpro.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewEmailFragment : BaseFragment() {
    private var _binding : FragmentNewEmailBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: ChangeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupObserve()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = getString(R.string.configuration)
            navigationIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_back)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupObserve() {
        viewmodel.updateResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    // Handle successful update
                    showLoading(false)
                    showToast("Update email successful")
                    findNavController().navigateUp()
                }
                is ApiResult.Error -> {
                    // Handle update error
                    showLoading(false)
                    showToast("Update failed: ${result.message}")
                }
                is ApiResult.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun setupAction() {
       binding.btnUpdate.setOnClickListener{
           val newEmail = binding.tvNewEmail.text.toString().trim()
           val currentPassword = binding.tvCurrentPassword.text.toString().trim()
           if(checkCredentials(newEmail,currentPassword)){
               viewmodel.updateEmail(newEmail, currentPassword)
           }else{
               showToast("Invalid credentials")
           }
       }
    }

    private fun checkCredentials(email: String, currentPassword: String): Boolean {
        return email.isNotEmpty() && currentPassword.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() && currentPassword.length >= 8
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.isVisible = state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}