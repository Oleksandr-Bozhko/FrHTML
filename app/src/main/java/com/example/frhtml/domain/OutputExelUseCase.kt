package com.example.frhtml.domain

class OutputExelUseCase(private val repository: Repository) {
    operator fun invoke(textList: Set<String>, fileName: String)=
        repository.createExcelFile(textList, fileName)
    }
