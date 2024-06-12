package com.example.TodoApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.TodoApp.ToDoApp.Data.TaskViewModel
import com.example.TodoApp.ToDoApp.RootNav
import com.example.TodoApp.ui.theme.OnlineToDoAppTheme

class MainActivity : ComponentActivity() {

    private val taskViewModel:TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlineToDoAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    RootNav(navController = navController, taskViewModel = taskViewModel,this)
                }
            }
        }
    }
}

