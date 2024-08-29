package com.xcheko51x.crud_firebase_compose_ejemplo

data class Tarea(
    var id: String = "",
    val titulo: String = "",
    val descripcion: String = ""
) {
    fun toMap(): Map<String, String> {
        return mapOf(
            "titulo" to titulo,
            "descripcion" to descripcion
        )
    }
}