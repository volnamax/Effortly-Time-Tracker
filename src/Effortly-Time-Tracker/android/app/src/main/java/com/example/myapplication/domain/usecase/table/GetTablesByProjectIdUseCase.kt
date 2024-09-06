package com.example.myapplication.domain.usecase.table

import com.example.myapplication.domain.model.Table
import com.example.myapplication.domain.repository.TableRepository

class GetTablesByProjectIdUseCase(private val tableRepository: TableRepository) {
    suspend operator fun invoke(projectId: Int): List<Table> {
        return tableRepository.getTablesByProjectId(projectId)
    }
}
