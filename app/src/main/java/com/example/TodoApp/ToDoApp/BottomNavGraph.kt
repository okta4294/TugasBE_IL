package com.example.TodoApp.ToDoApp

import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.TodoApp.ToDoApp.Data.TaskViewModel
import com.example.TodoApp.ToDoApp.Routes.Routes
import com.example.TodoApp.ToDoApp.presentation.BottomNavScreen
import com.example.TodoApp.ToDoApp.presentation.BottomScreens.AddTask
import com.example.TodoApp.ToDoApp.presentation.BottomScreens.AllTask
import com.example.TodoApp.ToDoApp.presentation.BottomScreens.CompletedTask
import com.example.TodoApp.ToDoApp.presentation.BottomScreens.EditTask
import com.example.TodoApp.ToDoApp.presentation.BottomScreens.Profile
import com.example.TodoApp.ToDoApp.presentation.LoginScreen
import com.example.TodoApp.ToDoApp.presentation.MainScreen
import com.example.TodoApp.ToDoApp.presentation.SignupScreen
import com.example.TodoApp.ToDoApp.presentation.SplashScreen


fun NavGraphBuilder.AuthNav(navController: NavHostController,context: Context) {
    navigation(startDestination = Routes.Signup.route, route = Routes.AUTH.route) {
        composable("signup") { SignupScreen(navController = navController,context) }
        composable("login") { LoginScreen(navController = navController) }
    }
}


@Composable
fun BottomNav(navController: NavHostController, taskViewModel: TaskViewModel, context: Context) {
    NavHost(navController = navController, startDestination = Routes.splash.route,
        enterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        } ) {

        composable(Routes.splash.route) { SplashScreen(navController = navController) }

        AuthNav(navController,context)

        composable(BottomNavScreen.All.route) {
            AllTask(
                navController = navController,
                taskViewModel,
                context
            )
        }
        composable(BottomNavScreen.Completed.route) {
            CompletedTask(
                navController = navController,
                taskViewModel
            )
        }
        composable(BottomNavScreen.Profile.route) {
            Profile(
                navController = navController,
                context
            )
        }

        composable(Routes.AddTask.route) { AddTask(navController = navController, taskViewModel,context) }

        composable(
            route = "${Routes.EditTask.route}/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->

            val taskId = backStackEntry.arguments?.getString("taskId")
            EditTask(
                navController = navController,
                taskId = taskId ?: "",
                taskViewModel = taskViewModel
            )
        }
    }
}

@Composable
fun RootNav(navController: NavHostController, taskViewModel: TaskViewModel, context: Context) {
    NavHost(navController = navController, startDestination = Routes.MAIN.route,
        enterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        }
        ) {

        composable(Routes.MAIN.route) {
            MainScreen(taskViewModel, context)
        }
    }
}