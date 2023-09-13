package com.yash.notesapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.yash.notesapp.R
import com.yash.notesapp.adapters.NotesAdapter
import com.yash.notesapp.databinding.FragmentMainBinding
import com.yash.notesapp.models.NoteResponse
import com.yash.notesapp.utils.NetworkResult
import com.yash.notesapp.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var adapter: NotesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = NotesAdapter(::onNoteClicked)
        
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success ->{
                    adapter.submitList(it.data)
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
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter
        noteViewModel.getNotes()
        binding.addNote.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
    }

    private fun onNoteClicked(noteResponse: NoteResponse){
        val bundle = Bundle()
        bundle.putString("note",Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment,bundle)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}