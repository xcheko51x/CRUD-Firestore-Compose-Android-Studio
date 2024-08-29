package com.xcheko51x.crud_firebase_compose_ejemplo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class TareaViewModel: ViewModel() {

    private val db = Firebase.firestore

    private var _listaTareas = MutableStateFlow<List<Tarea>>(emptyList())
    val listaTareas = _listaTareas.asStateFlow()

    //private var _listaTareas = MutableLiveData<List<Tarea>>()
    //val listaTareas: LiveData<List<Tarea>> get() = _listaTareas

    init {
        obtenerTareas()
    }

    fun obtenerTareas() {
        viewModelScope.launch(Dispatchers.IO) {
            val resultado = db.collection("tareas").get().await()
            val tareas = resultado.documents.mapNotNull {
                it.toObject(Tarea::class.java)
            }
            _listaTareas.value = tareas
        }
    }

    fun agregarTarea(tarea: Tarea) {

        tarea.id = UUID.randomUUID().toString()

        viewModelScope.launch(Dispatchers.IO) {
            db.collection("tareas").document(tarea.id).set(tarea)
                .addOnSuccessListener {
                    obtenerTareas()
                }
        }
    }

    fun actualizarTarea(tarea: Tarea) {

        viewModelScope.launch(Dispatchers.IO) {
            db.collection("tareas").document(tarea.id).update(tarea.toMap())
                .addOnSuccessListener {
                    obtenerTareas()
                }
        }
    }

    fun borrarTarea(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("tareas").document(id).delete()
                .addOnSuccessListener {
                    _listaTareas.value = listaTareas.value.filter {
                        it.id != id
                    }
                }
        }
    }
}