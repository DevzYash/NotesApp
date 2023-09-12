package com.yash.notesapp.viewmodel

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.notesapp.api.UserApi
import com.yash.notesapp.models.UserRequest
import com.yash.notesapp.models.UserResponse
import com.yash.notesapp.repository.UserRepository
import com.yash.notesapp.utils.Constants
import com.yash.notesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository):ViewModel() {

    val userReponseLiveData:LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData


    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateCredentials(username:String,email:String,password:String,isLogin:Boolean):Pair<Boolean,String>{
        var result = Pair(true,"")
        if ((!isLogin&&TextUtils.isEmpty(username))|| TextUtils.isEmpty(password)|| TextUtils.isEmpty(email)){
            result = Pair(false,"Please provide all credentials")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false,"Please provide valid email address")
        }
        else if(password.length<=5){
            result  = Pair(false,"Password should be greater than 5")
        }
        return result
    }

}