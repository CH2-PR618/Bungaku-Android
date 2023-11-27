package com.nccdms.bloomiqpro.ui.register

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nccdms.bloomiqpro.databinding.FragmentRegisterBinding
import com.nccdms.bloomiqpro.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
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

    private fun setupAction() {
        binding.apply {
            btnSignUp.setOnClickListener{
                val username = binding.usernameEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                if(checkCredentials(username, email, password)){
                    //viewmodel
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                }else{
                    showToast("Invalid credential")
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}