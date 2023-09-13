package com.yash.notesapp.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.yash.notesapp.api.NotesApi
import com.yash.notesapp.models.NoteRequest
import com.yash.notesapp.models.NoteResponse
import com.yash.notesapp.utils.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesApi: NotesApi) {

    private var _notesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLiveData: MutableLiveData<NetworkResult<List<NoteResponse>>>
        get() = _notesLiveData
    private var _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: MutableLiveData<NetworkResult<String>>
        get() = _statusLiveData


    suspend fun getNotes() {
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.getNotes()
        if (response.isSuccessful && response.body() != null) {
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val error = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResult.Error(error.getString("message")))
        } else {
            _notesLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.createNote(noteRequest)
        handleResponse(response, "Note Created")
    }


    suspend fun deleteNote(noteId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.deleteNote(noteId)
        handleResponse(response, "Note Deleted")
    }

    suspend fun updateNote(noteId: String, noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.updateNote(noteId, noteRequest)
        handleResponse(response, "Note Updated")
    }

    private fun handleResponse(response: Response<NoteResponse>, successMessage: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(successMessage))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }


}