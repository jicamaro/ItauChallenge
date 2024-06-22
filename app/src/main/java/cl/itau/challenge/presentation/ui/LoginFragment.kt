package cl.itau.challenge.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cl.itau.challenge.R
import cl.itau.challenge.databinding.FragmentLoginBinding
import cl.itau.challenge.domain.model.InvalidEmailException
import cl.itau.challenge.domain.model.InvalidPasswordException
import cl.itau.challenge.presentation.model.UiState
import cl.itau.challenge.presentation.utilities.gone
import cl.itau.challenge.presentation.utilities.visible
import cl.itau.challenge.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }

        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.buttonLogin.setOnClickListener {
            mainViewModel.signIn(
                binding.emailEdittext.text.toString(),
                binding.passwordEdittext.text.toString()
            )
        }
        binding.emailEdittext.setOnFocusChangeListener { _, b ->
            if (b) binding.emailInput.error = null
        }
        binding.passwordEdittext.setOnFocusChangeListener { _, b ->
            if (b) binding.passwordInput.error = null
        }
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.signInUiState.flowWithLifecycle(
                viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
            ).collect { uiState ->
                when (uiState) {
                    is UiState.SignInError -> {
                        when (uiState.throwable) {
                            is InvalidEmailException -> binding.emailInput.error = uiState.throwable.message
                            is InvalidPasswordException -> binding.passwordInput.error = uiState.throwable.message
                        }

                        binding.run {
                            progress.gone()
                            content.visible()
                        }
                    }
                    UiState.SignInIdle -> binding.run {
                        progress.gone()
                        content.visible()
                    }
                    UiState.SignInLoading -> binding.run {
                        progress.visible()
                        content.gone()
                    }
                    UiState.SignInSuccessful -> {
                        findNavController().navigate(
                            resId = R.id.action_loginFragment_to_dashboardFragment
                        )
                    }
                    else -> { }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}