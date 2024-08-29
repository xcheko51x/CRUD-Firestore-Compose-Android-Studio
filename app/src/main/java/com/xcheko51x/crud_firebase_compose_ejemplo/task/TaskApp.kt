package com.xcheko51x.crud_firebase_compose_ejemplo.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import java.util.UUID


@Composable
fun TaskApp() {
    val db = Firebase.firestore
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskDescription by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Fetch tasks from Firestore
    LaunchedEffect(Unit) {
        db.collection("tasks").get()
            .addOnSuccessListener { snapshot ->
                tasks = snapshot.documents.mapNotNull { it.toObject(Task::class.java) }
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Tasks", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = newTaskTitle,
            onValueChange = { newTaskTitle = it },
            label = { Text("Title") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = newTaskDescription,
            onValueChange = { newTaskDescription = it },
            label = { Text("Description") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val task = Task(
                id = UUID.randomUUID().toString(),
                title = newTaskTitle,
                description = newTaskDescription
            )
            coroutineScope.launch {
                db.collection("tasks").document(task.id).set(task)
                    .addOnSuccessListener {
                        tasks = tasks + task
                        newTaskTitle = ""
                        newTaskDescription = ""
                    }
            }
        }) {
            Text("Add Task")
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                TaskItem(task, db) { id ->
                    coroutineScope.launch {
                        db.collection("tasks").document(id).delete()
                            .addOnSuccessListener {
                                tasks = tasks.filter { it.id != id }
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, db: FirebaseFirestore, onDelete: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
            Text(text = task.description)
        }
        IconButton(onClick = { onDelete(task.id) }) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}