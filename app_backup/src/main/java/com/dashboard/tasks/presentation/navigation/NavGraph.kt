package com.dashboard.tasks.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dashboard.tasks.auth.presentation.screens.LoginScreen
import com.dashboard.tasks.presentation.screens.CreateTaskScreen
import com.dashboard.tasks.presentation.screens.DashboardScreen
import com.dashboard.tasks.presentation.screens.TaskDetailScreen
import com.dashboard.tasks.presentation.screens.TaskListScreen

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = Routes.LOGIN) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }}
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateToTasks = { navController.navigate(Routes.TASK_LIST) },
                onNavigateToCreate = { navController.navigate(Routes.CREATE_TASK) }
            )
        }

        composable(Routes.TASK_LIST) {
            TaskListScreen(
                onTaskClick = { taskId -> navController.navigate(Routes.taskDetail(taskId)) },
                onCreateClick = { navController.navigate(Routes.CREATE_TASK) }
            )
        }

        composable(
            route = Routes.TASK_DETAIL,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            TaskDetailScreen(
                taskId = backStackEntry.arguments?.getString("taskId") ?: "",
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CREATE_TASK) {
            CreateTaskScreen(
                onTaskCreated = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
