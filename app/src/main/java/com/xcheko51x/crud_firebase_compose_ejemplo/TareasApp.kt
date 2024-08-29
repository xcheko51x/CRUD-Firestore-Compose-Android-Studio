package com.xcheko51x.crud_firebase_compose_ejemplo

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun TareasApp() {

    val viewModel = TareaViewModel()

    var idUpdate by remember { mutableStateOf("") }

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    val listaTareas by viewModel.listaTareas.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "TAREAS",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Titulo") }
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripcion") }
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        Button(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            onClick = {
            val tarea = Tarea(
                titulo = titulo,
                descripcion = descripcion
            )
            viewModel.agregarTarea(tarea)

            titulo = ""
            descripcion = ""
        }) {
            Text("Agregar Tarea")
        }

        Button(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            onClick = {
                val tarea = Tarea(
                    id = idUpdate,
                    titulo = titulo,
                    descripcion = descripcion
                )

                viewModel.actualizarTarea(tarea)

                idUpdate = ""
                titulo = ""
                descripcion = ""
            }) {
            Text("Editar Tarea")
        }

        Spacer(
            modifier = Modifier
                .height(16.dp)
        )

        LazyColumn {
            items(listaTareas) { tarea ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            idUpdate = tarea.id
                            titulo = tarea.titulo
                            descripcion = tarea.descripcion
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(text = tarea.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(text = tarea.descripcion)
                    }
                    IconButton(
                        onClick = {
                            viewModel.borrarTarea(tarea.id)
                        }
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Borrar")
                    }
                }
            }
        }

        /*LazyColumn {
            items(viewModel.listaTareas.value!!.toMutableList()) { tarea ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = tarea.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(text = tarea.descripcion)
                    }
                    IconButton(
                        onClick = {
                            viewModel.borrarTarea(tarea.id)
                        }
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Borrar")
                    }
                }
            }
        }*/
    }
}