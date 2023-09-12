package com.yash.notesapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yash.notesapp.api.UserApi
import com.yash.notesapp.models.UserRequest
import com.yash.notesapp.models.UserResponse
import com.yash.notesapp.utils.Constants.TAG
import com.yash.notesapp.utils.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData


    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        try {
            val response = userApi.signup(userRequest)
            handleResponse(response)
        }
        catch (e: Exception) {

            Log.e(TAG, e.toString())
        }
    }

    private fun handleResponse(response: Response<UserResponse>) {

        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val error = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(error.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }

        Log.d(TAG, response.body().toString())
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        try {
            val response = userApi.signin(userRequest)
            handleResponse(response)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}