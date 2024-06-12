package com.example.TodoApp.ToDoApp.presentation.BottomScreens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.TodoApp.R
import com.example.TodoApp.ToDoApp.CustomNoteDesign.NoteItem
import com.example.TodoApp.ToDoApp.Data.TaskViewModel
import com.example.TodoApp.ToDoApp.Routes.Routes
import com.example.TodoApp.ui.theme.BabyBlue
import com.example.TodoApp.ui.theme.DefaultColor
import com.example.TodoApp.ui.theme.LightBlue
import com.example.TodoApp.ui.theme.MintGreen
import com.example.TodoApp.ui.theme.PurpleDark
import com.example.TodoApp.ui.theme.PurpleLight
import com.example.TodoApp.ui.theme.RedDone
import com.example.TodoApp.ui.theme.RedOrange
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTask(navController: NavController, taskViewModel: TaskViewModel, context: Context) {

    val currentDate = LocalDate.now()
    val tasks by taskViewModel.tasks.collectAsState()

    val currentUser = FirebaseAuth.getInstance().currentUser

    val scope = rememberCoroutineScope()



    DisposableEffect(Unit) {
        if (currentUser != null) {
            val userId = currentUser.uid
            taskViewModel.fetchTasks(userId)
        }
        onDispose { }
    }


    Scaffold(
        modifier = Modifier
            .background(PurpleLight)
            .padding(bottom = 75.dp),

        floatingActionButton = {
            FloatingActionButton(
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .size(65.dp),
                shape = CircleShape,
                containerColor = PurpleDark,
                onClick = {
                    navController.navigate(Routes.AddTask.route)
                })
            {
                Text(
                    text = "+",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp
                )
            }
        },
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(PurpleDark)
                    .padding(start = 8.dp, end = 8.dp),
                title = {
                    Text(
                        text = "TODO APP",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PurpleDark),
                actions = {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.claenders),
                            contentDescription = null,
                            tint = Color.White
                        )
                        Column(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(top = 4.dp)
                                .background(PurpleDark),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = currentDate.dayOfMonth.toString(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .background(PurpleLight)
                .padding(top = 4.dp, bottom = 0.dp, start = 15.dp, end = 15.dp)
        ) {
            LazyColumn {
                items(tasks) { task ->

                    val editTask = SwipeAction(
                        onSwipe = {
                            if(!task.completed){
                                scope.launch {
                                    delay(400)
                                    navController.navigate("${Routes.EditTask.route}/${task.id}")
                                    taskViewModel.setCurrentTask(task)
                                }
                            }
                            else{
                                Toast.makeText(context,"Task is completed, can't be edited.",Toast.LENGTH_SHORT).show()
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.vector_10),
                                contentDescription = null,
                                tint = Color.White, modifier = Modifier.padding(16.dp)
                            )
                        },
                        background = MintGreen,
                    )

                    val delete = SwipeAction(
                        onSwipe = {
                            scope.launch {
                                delay(400)
                                taskViewModel.deleteTask(task.id, context)
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.vector__5_),
                                contentDescription = null,
                                tint = Color.White, modifier = Modifier.padding(16.dp)
                            )
                        },
                        background = RedDone,
                    )

                    SwipeableActionsBox(
                        startActions = listOf(editTask),
                        endActions = listOf(delete),
                        swipeThreshold = 90.dp,
                        modifier = Modifier.padding(top = 15.dp),
                        backgroundUntilSwipeThreshold =
                        if (task.color == "default") DefaultColor.copy(.6f)
                        else if (task.color == "lightBlue") LightBlue.copy(.6f)
                        else if (task.color == "babyBlue") BabyBlue.copy(.6f)
                        else RedOrange.copy(.6f)
                    )
                    {

                        NoteItem(
                            onDeleteClick = {

                                scope.launch {
                                    delay(400)
                                    taskViewModel.deleteTask(task.id, context)
                                }

                            },
                            onEditClick = {
                                navController.navigate("${Routes.EditTask.route}/${task.id}")
                                taskViewModel.setCurrentTask(task)
                            },
                            onCompleteClick = {
                                taskViewModel.toggleTaskCompletion(task.id)
                                if (!task.completed) {
                                    Toast.makeText(
                                        context,
                                        "Task completed",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Task completion unchecked,",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            },
                            checkColor = if (task.completed) RedDone else Color.Unspecified,
                            editColor = if (task.completed) Color.Gray else Color.Unspecified,
                            title = task.title,
                            description = task.description,
                            deleteIcon = R.drawable.vector__5_,
                            checkIcon = R.drawable.vector__6_,
                            editIcon = R.drawable.vector_10,
                            bgColor =
                            if (task.color == "babyBlue") BabyBlue
                            else if (task.color == "lightBlue") LightBlue
                            else if (task.color == "redOrange") RedOrange
                            else DefaultColor,
                            editButtonEnabled = !task.completed
                        )
                    }

                }
            }
        }
    }
}