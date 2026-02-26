package com.dashboard.tasks.presentation.navigation

object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val TASK_LIST = "task_list"
    const val TASK_DETAIL = "task_detail/{taskId}"
    const val CREATE_TASK = "create_task"
    const val PROFILE = "profile"

    fun taskDetail(taskId: String) = "task_detail/$taskId"
}
