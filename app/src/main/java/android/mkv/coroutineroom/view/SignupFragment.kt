package android.mkv.coroutineroom.view

import android.mkv.coroutineroom.R
import android.mkv.coroutineroom.databinding.FragmentSignupBinding
import android.mkv.coroutineroom.viewmodel.SignupViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding

    private lateinit var viewModel: SignupViewModel
    private val TAG = "SignupFragment"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        event()
        observeViewModel()

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
    }

    private fun event() {
        binding.apply {
            btnSignup.setOnClickListener {
                val username = inputUsername.text.toString()
                val password = inputPassword.text.toString()
                val info = inputInfo.text.toString()
                if (username.isEmpty() || password.isEmpty() || info.isEmpty()) Toast.makeText(
                    requireContext(),
                    "Please fill all fields!",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    viewModel.signup(username, password, info)
                }
            }

            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.loginFragment)
            }

            viewModel.getList()
        }
    }

    private fun observeViewModel() {
        viewModel.signupComplete.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Signup complete!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.mainFragment)
        }
        viewModel.error.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
        }

        viewModel.allList.observe(viewLifecycleOwner) { list ->
            Log.i(TAG, "observeViewModel: $list")
        }
    }
}