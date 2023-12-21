package com.nccdms.bloomiqpro.ui.auth.register

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.databinding.FragmentRegisterBinding
import com.nccdms.bloomiqpro.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            showLoading(false)
            viewmodel.registerResult.collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        showLoading(false)
                        showToast("Register successful")
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                    }
                    is ApiResult.Error -> {
                        showLoading(false)
                        showToast("Register failed: ${result.message}")
                    }
                    is ApiResult.Loading -> {
                        showLoading(true)
                    }
                }
                Log.d("RegisterFragment", "setupObserve: Result received - $result")
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            btnSignUp.setOnClickListener{
                val username = binding.usernameEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val confirmPassword = binding.confirmPasswordEditText.text.toString()
                if(confirmPassword != password){
                    showToast("Password not match")
                }else{
                    if(checkCredentials(username, email, password)){
                        viewmodel.register(email = email, password = password, nama = username, confirmPass = confirmPassword)
                        setupObserve()
                    }else{
                        showToast("Invalid credential")
                    }
                }
            }
            tvLogin.setOnClickListener{
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }
        }
    }

    private fun checkCredentials(username:String,email: String, password: String): Boolean {
        return email.isNotEmpty() &&
                password.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length >= 8 &&
                username.isNotEmpty()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.isVisible = state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}