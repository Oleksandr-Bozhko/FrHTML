package com.example.frhtml.domain

import javax.inject.Inject

class ExtractRussianTextUseCase  @Inject constructor(private val repository: Repository) {
    operator fun invoke (html: String)=
      repository.extractRussianText(html)
    }
