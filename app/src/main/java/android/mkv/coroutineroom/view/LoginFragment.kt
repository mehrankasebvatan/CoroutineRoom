package android.mkv.coroutineroom.view

import android.mkv.coroutineroom.R
import android.mkv.coroutineroom.databinding.FragmentLoginBinding
import android.mkv.coroutineroom.viewmodel.LoginViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        event()
        observeViewModel()

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private fun event() {
        binding.apply {
            btnLogin.setOnClickListener {
                val username = inputUsername.text.toString()
                val password = inputPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) Toast.makeText(
                    requireContext(),
                    "Please fill all fields!",
                    Toast.LENGTH_SHORT
                ).show()
                else viewModel.login(username, password)
            }

            btnSignup.setOnClickListener {
                findNavController().navigate(R.id.signupFragment)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loginComplete.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.mainFragment)
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
        }
    }
}