package cl.itau.challenge.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cl.itau.challenge.R
import cl.itau.challenge.databinding.FragmentRegisterBinding
import cl.itau.challenge.domain.model.InvalidEmailException
import cl.itau.challenge.domain.model.InvalidLastNameException
import cl.itau.challenge.domain.model.InvalidNameException
import cl.itau.challenge.domain.model.InvalidPasswordException
import cl.itau.challenge.presentation.model.UiState
import cl.itau.challenge.presentation.utilities.gone
import cl.itau.challenge.presentation.utilities.visible
import cl.itau.challenge.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()

        (requireActivity() as AppCompatActivity).run {
            onBackPressedDispatcher.addCallback {
                findNavController().navigateUp()
            }
        }

        binding.buttonRegister.setOnClickListener {
            mainViewModel.signUp(
                id = UUID.randomUUID().toString(),
                name = binding.nameEdittext.text.toString(),
                lastName = binding.lastNameEdittext.text.toString(),
                email = binding.emailEdittext.text.toString(),
                password = binding.passwordEdittext.text.toString()
            )
        }
        binding.nameEdittext.setOnFocusChangeListener { _, b ->
            if (b) binding.nameInput.error = null
        }
        binding.lastNameEdittext.setOnFocusChangeListener { _, b ->
            if (b) binding.lastNameInput.error = null
        }
        binding.emailEdittext.setOnFocusChangeListener { _, b ->
            if (b) binding.emailInput.error = null
        }
        binding.passwordEdittext.setOnFocusChangeListener { _, b ->
            if (b) binding.passwordInput.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.signUpUiState.flowWithLifecycle(
                viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
            ).collect { uiState ->
                when (uiState) {
                    is UiState.SignUpError -> {
                        when (uiState.throwable) {
                            is InvalidNameException -> binding.nameInput.error = uiState.throwable.message
                            is InvalidLastNameException -> binding.lastNameInput.error = uiState.throwable.message
                            is InvalidEmailException -> binding.emailInput.error = uiState.throwable.message
                            is InvalidPasswordException -> binding.passwordInput.error = uiState.throwable.message
                        }

                        binding.run {
                            progress.gone()
                            content.visible()
                        }
                    }
                    UiState.SignUpIdle -> binding.run {
                        progress.gone()
                        content.visible()
                    }
                    UiState.SignUpLoading -> binding.run {
                        progress.visible()
                        content.gone()
                    }
                    UiState.SignUpSuccessful -> {
                        findNavController().navigate(
                            resId = R.id.action_registerFragment_to_loginFragment
                        )
                    }
                    else -> { }
                }
            }
        }
    }
}