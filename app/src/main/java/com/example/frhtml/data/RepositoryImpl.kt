package com.example.frhtml.data

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.frhtml.domain.Repository
import com.example.frhtml.domain.TranslationEntity
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(): Repository {
    override fun getListOfBrands(): List<Brand> {
        return listOf(
            Brand("barbour"),
            Brand("gramicci"),
            Brand("isseymiyake"),
            Brand("lemaire"),
            Brand("norrona"),
            Brand("oakley"),
            Brand("onitsuka-tiger")
        )
    }

    override fun loadHtmlFromAssets(context: Context, fileName: String): String {
        return context.assets.open("$fileName.html").use { inputStream ->
            inputStream.bufferedReader().use(BufferedReader::readText)
        }
    }

    override fun extractRussianText(html: String): Set<String> {
        val textBetweenTagsRegex = Regex("(?<=>)[^<>]+(?=<)")
        val textInsideTagsRegex = Regex("(?<=\")[^\"]+(?=\")")
        val cyrillicLetterRegex = Regex("[А-Яа-яЁё]")
        val unwantedHtmlTagsRegex = Regex("<\\/?[^>]+>")

        val textBetweenTags = textBetweenTagsRegex.findAll(html)
            .map { it.value.trim() }
            .filter { it.isNotEmpty() && cyrillicLetterRegex.containsMatchIn(it) }
            .toSet()


        val textInsideTags = textInsideTagsRegex.findAll(html)
            .map { it.value.trim() }
            .filter { it.isNotEmpty() && cyrillicLetterRegex.containsMatchIn(it) }
            .filterNot { unwantedHtmlTagsRegex.containsMatchIn(it) } // Исключаем строки с HTML-тегами
            .toSet()

        return textBetweenTags + textInsideTags
    }

    override fun createExcelFile(textList: Set<String>, fileName: String) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Data")

        textList.forEachIndexed { index, value ->
            val row = sheet.createRow(index)
            val cell = row.createCell(0)
            cell.setCellValue(value)
        }

        try {
            val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
            val file = File(downloadsPath, "$fileName.xlsx")
            FileOutputStream(file).use { outputStream ->
                workbook.write(outputStream)
                println("Файл сохранен: ${file.absolutePath}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                workbook.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun readCsv(context: Context, fileName: String): List<TranslationEntity> {
        val translations = mutableListOf<TranslationEntity>()
        var id = 1
        try {
            context.assets.open("$fileName.csv").bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    val tokens = line.split(";")
                    if (tokens.size >= 2) {
                        var originalText = tokens[0]
                        var translatedText = tokens[1]

                        // Удаление обрамляющих двойных кавычек
                        originalText = originalText.removeSurrounding("\"")
                        translatedText = translatedText.removeSurrounding("\"")

                        // Замена двойных кавычек на одинарные
                        originalText = originalText.replace("\"\"", "\"")
                        translatedText = translatedText.replace("\"\"", "\"")

                        translations.add(TranslationEntity(id, originalText, translatedText))
                        id++
                    }
                }
            }
        } catch (e: IOException) {
            throw e
        }
        translations.forEach { translation ->
            Log.d(
                "TranslationEntity",
                "ID: ${translation.id}, Original: ${translation.originalText}, Translated: ${translation.translatedText}"
            )
        }
        return translations
    }

    override fun replaceTextInHtml(html: String, translations: List<TranslationEntity>): String {
        val sortedTranslations = translations.sortedByDescending { it.originalText.length }

        // Поочередная замена текста, начиная с самых длинных фраз
        var modifiedHtml = html
        sortedTranslations.forEach { translation ->
            modifiedHtml = modifiedHtml.replace(translation.originalText, translation.translatedText)
        }
        return modifiedHtml
    }

    override fun saveHtmlToFile(context: Context, htmlContent: String, fileName: String) {
        context.openFileOutput("$fileName.html", Context.MODE_PRIVATE).use { outputStream ->
            outputStream.write(htmlContent.toByteArray())
    }
}}