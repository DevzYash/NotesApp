package com.yash.notesapp.api

import com.yash.notesapp.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var manager: TokenManager
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = manager.getToken()
        request.header("Authorization","Bearer $token")
        return chain.proceed(request.build())
    }
}