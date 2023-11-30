package com.example.frhtml.domain

class ExtractRussianTextUseCase (private val repository: Repository) {
    operator fun invoke (html: String)=
      repository.extractRussianText(html)
    }
