package com.example.frhtml.presentation

import android.content.Context
import android.os.Environment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream

class Test2 {

    fun extractTextFromHtmlFile(context: Context, fileName: String): Set<String> {
        val htmlString = loadHtmlFromAssets(context, fileName)
        val textList = extractRussianText1(htmlString)

        createExcelFile(textList, fileName)

        return textList
    }

    private fun createExcelFile(data: Set<String>, fileName: String) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Data")

        data.forEachIndexed { index, value ->
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

    private fun loadHtmlFromAssets(context: Context, fileName: String): String {
        return context.assets.open("$fileName.html").use { inputStream ->
            inputStream.bufferedReader().use(BufferedReader::readText)
        }
    }
}
private fun extractRussianText1(html: String): Set<String> {
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