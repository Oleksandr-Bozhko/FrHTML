package com.example.frhtml.domain

import android.content.Context
import javax.inject.Inject

class OutputHtmlUseCase  @Inject constructor(private val repository: Repository) {
    operator fun invoke(context: Context, htmlFileName: String) {
        val htmlContent = repository.loadHtmlFromAssets(context, htmlFileName)
        val translations = repository.readCsv(context, htmlFileName)
        val modifiedHtml = repository.replaceTextInHtml(htmlContent, translations)
        repository.saveHtmlToFile(context, modifiedHtml, htmlFileName)
    }
}