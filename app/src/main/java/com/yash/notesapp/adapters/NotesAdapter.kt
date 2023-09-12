package com.yash.notesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yash.notesapp.databinding.NoteItemBinding
import com.yash.notesapp.models.NoteResponse

class NotesAdapter() : ListAdapter<NoteResponse, NotesAdapter.NotesViewHolder>(DiffUtils()) {
    inner class NotesViewHolder(private val binding:NoteItemBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(note:NoteResponse){
            binding.title.text =note.title
            binding.desc.text =note.description
        }

    }

    class DiffUtils : DiffUtil.ItemCallback<NoteResponse>() {
        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
           return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
           return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note!!)
    }

}