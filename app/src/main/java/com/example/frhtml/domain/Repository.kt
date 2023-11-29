package com.example.frhtml.domain

import android.content.Context
import com.example.frhtml.data.Brand

interface Repository {
    fun getListOfBrands(): List<Brand>

    fun loadHtmlFromAssets(context: Context, fileName: String): String

    fun extractRussianText(html: String): Set<String>

    fun createExcelFile(textList: Set<String>, fileName: String)

    fun readCsv(context: Context, fileName: String): List<TranslationEntity>

    fun replaceTextInHtml(html: String, translations: List<TranslationEntity>): String

    fun saveHtmlToFile(context: Context, htmlContent: String, fileName: String)
}
