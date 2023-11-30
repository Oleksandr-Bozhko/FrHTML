package com.example.frhtml.domain

import android.content.Context

class ReadCsvUseCase (private val repository: Repository) {
    operator fun invoke(context: Context, fileName: String) =
        repository.readCsv(context, fileName)
}


