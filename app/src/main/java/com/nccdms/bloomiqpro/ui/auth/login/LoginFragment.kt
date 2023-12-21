package com.nccdms.bloomiqpro.ui.auth.login

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
import com.nccdms.bloomiqpro.databinding.FragmentLoginBinding
import com.nccdms.bloomiqpro.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private var _binding:FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            showLoading(false)
            viewmodel.loginResult.collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        showLoading(false)
                        showToast("Login successful")
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
                    }
                    is ApiResult.Error -> {
                        showLoading(false)
                        showToast("Login failed: ${result.message}")
                    }
                    is ApiResult.Loading -> {
                        showLoading(true)
                    }
                }
                Log.d("LoginFragment", "setupObserve: Result received - $result")
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            tvRegister.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
            btnLogin.setOnClickListener{
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                Log.d("LoginFragment","$email & $password")
                if(checkCredentials(email,password)){
                    viewmodel.login(email, password)
                    setupObserve()
                }else{
                    showToast("Invalid credentials")
                }
            }
        }
    }

    private fun checkCredentials(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.isVisible = state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}