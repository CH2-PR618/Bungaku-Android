package com.nccdms.bloomiqpro.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nccdms.bloomiqpro.databinding.FragmentLoginBinding
import com.nccdms.bloomiqpro.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private var _binding:FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewmodel:LoginViewModel by viewModels()
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
        //setupObserve()
    }

   /* private fun setupObserve() {

    }*/

    private fun setupAction() {
        binding.apply {
            tvRegister.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
            btnLogin.setOnClickListener{
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                if(checkCredentials(email,password)){
                    viewmodel.setToken("sample token")
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}