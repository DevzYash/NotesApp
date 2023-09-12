package com.yash.notesapp

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yash.notesapp.api.NotesApi
import com.yash.notesapp.databinding.FragmentLoginBinding
import com.yash.notesapp.databinding.FragmentMainBinding
import com.yash.notesapp.utils.Constants
import com.yash.notesapp.utils.Constants.TAG
import com.yash.notesapp.utils.NetworkResult
import com.yash.notesapp.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success ->{

                }
                is NetworkResult.Error ->{
                    MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Error Occurred")
                        .setMessage(it.message)
                        .setPositiveButton("GOT IT") { dialogInterface, i -> }
                        .show()
                }
                is NetworkResult.Loading->{
                    binding.progressBar.isVisible = true
                }
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}