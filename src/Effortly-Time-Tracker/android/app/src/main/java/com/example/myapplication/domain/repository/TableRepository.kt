package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Table

interface TableRepository {
    suspend fun getTablesByProjectId(projectId: Int): List<Table>
    suspend fun addTable(table: Table)
}