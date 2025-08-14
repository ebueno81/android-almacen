package com.example.almacen.feature_activity.data.remote.dto

data class UpsertActivityDetailsRequest(
    val toCreate: List<DetailToCreate> = emptyList(),
    val toUpdate: List<DetailToUpdate> = emptyList(),
    val toDelete: List<Int> = emptyList()
) {
    data class DetailToCreate(
        val idArticulo: Int,
        val nroLote: String,
        val peso: Double,
        val cajas: Int
    )
    data class DetailToUpdate(
        val id: Int,
        val idArticulo: Int,
        val nroLote: String,
        val peso: Double,
        val cajas: Int
    )
}