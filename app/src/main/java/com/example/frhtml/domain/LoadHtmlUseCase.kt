package com.example.frhtml.domain

import android.content.Context

class LoadHtmlFromAssetsUseCase(private val repository: Repository) {
    operator fun invoke(context: Context, fileName: String) =
        repository.loadHtmlFromAssets(context, fileName)
}