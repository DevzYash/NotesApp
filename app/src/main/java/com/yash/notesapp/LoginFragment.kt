package com.yash.notesapp

import android.R.layout
import android.app.ProgressDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.yash.notesapp.databinding.FragmentLoginBinding
import com.yash.notesapp.models.UserRequest
import com.yash.notesapp.utils.NetworkResult
import com.yash.notesapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var snackbar: Snackbar
    private lateinit var mProgressDialog: ProgressDialog
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        mProgressDialog = ProgressDialog(requireActivity())
        binding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snackbar = Snackbar.make(binding.root, "Please Wait", Snackbar.LENGTH_SHORT)
        val viewGroup = snackbar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text).parent as ViewGroup
        viewGroup.addView(ProgressBar(context).also {
            it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            it.foregroundGravity = Gravity.CENTER_VERTICAL
        })





        binding.signInButton.setOnClickListener {
           // snackbar.show()
            val validation_result = validateDetails()
            if (validation_result.first) {
                var email = binding.emailEditText.text.toString()
                var password = binding.passwordEditText.text.toString()
                authViewModel.loginUser(UserRequest(email, password, ""))
            } else {
                snackbar.dismiss()
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Something Wrong")
                    .setMessage(validation_result.second)
                    .setPositiveButton("GOT IT") { dialogInterface, i -> }
                    .show()
            }
        }

        binding.signUpTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        authViewModel.userReponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    snackbar.dismiss()
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    snackbar.dismiss()
                    MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Error Occurred")
                        .setMessage(it.message)
                        .setPositiveButton("GOT IT") { dialogInterface, i -> }
                        .show()
                }

                is NetworkResult.Loading -> {
                    snackbar.show()
                }
            }
        })
    }

    private fun validateDetails(): Pair<Boolean, String> {
        var email = binding.emailEditText.text.toString()
        var username = ""
        var password = binding.passwordEditText.text.toString()
        return authViewModel.validateCredentials(username, email, password, true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}