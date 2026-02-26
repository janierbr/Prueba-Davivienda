package com.dashboard.tasks.data.remote.api

import com.dashboard.tasks.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body credentials: Map<String, String>): Response<Map<String, String>>

    @POST("auth/register")
    suspend fun register(@Body body: Map<String, String>): Response<UserDto>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body body: Map<String, String>): Response<Map<String, String>>

    @GET("auth/me")
    suspend fun getCurrentUser(): Response<UserDto>
}
