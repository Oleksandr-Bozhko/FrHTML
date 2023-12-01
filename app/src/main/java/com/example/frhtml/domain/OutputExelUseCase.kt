package com.example.frhtml.domain

import javax.inject.Inject

class OutputExelUseCase  @Inject constructor(private val repository: Repository) {
    operator fun invoke(textList: Set<String>, fileName: String)=
        repository.createExcelFile(textList, fileName)
    }
