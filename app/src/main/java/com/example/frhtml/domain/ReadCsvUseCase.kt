package com.example.frhtml.domain

import android.content.Context
import javax.inject.Inject

class ReadCsvUseCase  @Inject constructor(private val repository: Repository) {
    operator fun invoke(context: Context, fileName: String) =
        repository.readCsv(context, fileName)
}


