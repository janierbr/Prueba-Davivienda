package com.dashboard.tasks.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("status") val status: String,
    @SerializedName("priority") val priority: String,
    @SerializedName("assigned_user_id") val assignedUserId: String,
    @SerializedName("project_id") val projectId: String,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("due_date") val dueDate: Long
)
