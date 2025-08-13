package com.example.almacen.catalog.data.remote.dto

data class PageResponse<T>(
    val content: List<T>,
    val totalElements: Long?,
    val totalPages: Int?,
    val number: Int?,         // página actual (0-based)
    val size: Int?,           // tamaño de página
    val first: Boolean?,
    val last: Boolean?
)