package com.example.myapplication.domain.usecase.table

import com.example.myapplication.domain.model.Table
import com.example.myapplication.domain.repository.TableRepository

class AddTableUseCase(private val tableRepository: TableRepository) {
    suspend operator fun invoke(table: Table) {
        tableRepository.addTable(table)
    }
}
