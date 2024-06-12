package com.example.TodoApp.ToDoApp.presentation

import android.content.Context
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.TodoApp.ToDoApp.BottomNav
import com.example.TodoApp.ToDoApp.Data.TaskViewModel
import com.example.TodoApp.ui.theme.PurpleLight

@Composable
fun MainScreen(taskViewModel: TaskViewModel,context: Context) {

    val navController = rememberNavController()

    val screens = listOf(
        BottomNavScreen.All,
        BottomNavScreen.Completed,
        BottomNavScreen.Profile
    )
    val showBottomBar = navController
        .currentBackStackEntryAsState().value?.destination?.route in screens.map { it.route }


    Scaffold(
        containerColor = PurpleLight,
        bottomBar = {
            if (showBottomBar) {
                CustomBottomNav(navController = navController,context)
            }

        }
    ) {
        it
        BottomNav(navController = navController, taskViewModel,context)
    }
}