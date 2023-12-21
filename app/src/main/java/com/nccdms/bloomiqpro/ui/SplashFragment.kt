package com.nccdms.bloomiqpro.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.nccdms.bloomiqpro.databinding.FragmentSplashBinding
import com.nccdms.bloomiqpro.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStartDestination()
    }

    private fun setStartDestination() {
        viewModel.sessionId.observe(viewLifecycleOwner) { sessionId ->
            Log.d("SessionKey","value key : ${sessionId}")
            val action = if (sessionId?.isNotEmpty() == true) {
                SplashFragmentDirections.actionSplashFragmentToMainActivity()
            } else {
                SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            }
            navigateWithDelay(action, DURATION)
        }
    }

    private fun navigateWithDelay(action: NavDirections, delay: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(action)
            if (viewModel.sessionId.value?.isNotEmpty() == true) {
                requireActivity().finish()
            }
        }, delay)
    }

    companion object {
        private const val DURATION: Long = 1500
    }
}
