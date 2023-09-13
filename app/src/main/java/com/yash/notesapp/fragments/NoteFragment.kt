package com.yash.notesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.yash.notesapp.databinding.FragmentNoteBinding
import com.yash.notesapp.models.NoteRequest
import com.yash.notesapp.models.NoteResponse
import com.yash.notesapp.utils.NetworkResult
import com.yash.notesapp.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private var note: NoteResponse? = null
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialSetup()
        onclickSetup()
        observers()
    }

    private fun observers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Error -> {
                    MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Error Occurred")
                        .setMessage(it.message)
                        .setPositiveButton("GOT IT") { dialogInterface, i -> }
                        .show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        })
    }

    private fun onclickSetup() {
        binding.btnDelete.setOnClickListener{
            note?.let {noteViewModel.deleteNote(it._id) }
        }
        binding.btnSubmit.setOnClickListener {
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(description,title)
            if (note != null) {
                noteViewModel.updateNote(note!!._id,noteRequest)
            }
            else{
                noteViewModel.createNote(noteRequest)
            }
        }
    }

    private fun initialSetup() {
        val jsonData = arguments?.getString("note")
        if (jsonData != null) {
            note = Gson().fromJson(jsonData,NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        } else {
            binding.addEditText.text = "Add Note"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}