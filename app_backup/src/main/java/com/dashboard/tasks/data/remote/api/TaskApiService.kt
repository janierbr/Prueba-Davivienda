package com.dashboard.tasks.data.remote.api

import com.dashboard.tasks.data.remote.dto.TaskDto
import retrofit2.Response
import retrofit2.http.*

interface TaskApiService {

    @GET("tasks")
    suspend fun getTasks(): Response<List<TaskDto>>

    @GET("tasks/{id}")
    suspend fun getTaskById(@Path("id") id: String): Response<TaskDto>

    @GET("tasks/user/{userId}")
    suspend fun getTasksByUser(@Path("userId") userId: String): Response<List<TaskDto>>

    @POST("tasks")
    suspend fun createTask(@Body task: TaskDto): Response<TaskDto>

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body task: TaskDto): Response<TaskDto>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Unit>
}
