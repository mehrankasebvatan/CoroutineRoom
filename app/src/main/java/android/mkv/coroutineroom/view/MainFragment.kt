package android.mkv.coroutineroom.view

import android.app.AlertDialog
import android.mkv.coroutineroom.R
import android.mkv.coroutineroom.databinding.FragmentMainBinding
import android.mkv.coroutineroom.model.LoginState
import android.mkv.coroutineroom.viewmodel.MainViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        event()
        observeViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun event() {
        binding.apply {

            txtUsername.text = LoginState.user?.username

            btnDeleteUser.setOnClickListener {
                activity?.let {
                    AlertDialog.Builder(it)
                        .setTitle("Delete User")
                        .setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("YES") { p0, p1 -> viewModel.onDeleteUser() }
                        .setNegativeButton("CANCEL", null)
                        .create()
                        .show()
                }
            }

            btnSignOut.setOnClickListener {
                viewModel.onSignOut()
            }
        }
    }

    private fun observeViewModel() {

        viewModel.userDelete.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Deleted user!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.signupFragment)

        }

        viewModel.signOut.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Signed Out!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.loginFragment)
        }

    }
}