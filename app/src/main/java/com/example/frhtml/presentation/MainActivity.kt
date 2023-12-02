package com.example.frhtml.presentation

//import com.example.frhtml.di.DaggerAppComponent
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.frhtml.databinding.ActivityMainBinding
import com.example.frhtml.domain.TranslationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel
       private val component by lazy {
           (application as App).component
      }

    override fun onCreate(savedInstanceState: Bundle?) {
             component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        val brands = listOf(
            "barbour",
            "gramicci",
            "isseymiyake",
            "lemaire",
            "norrona",
            "oakley",
            "onitsuka-tiger",
            "the-row"
        )
        val test2 = Test2()

        binding.buttonOutputExel.setOnClickListener {

            brands.forEach { brand ->
                test2.extractTextFromHtmlFile(this, brand)
            }
        }
        binding.buttonOutputHTML.setOnClickListener {
            brands.forEach { brand ->

                processHtmlFile(this, brand)
            }
        }

        binding.buttonLoadCsv.setOnClickListener {
            readCsvFile(this, brands[6])
        }
    }


}

fun readCsvFile(context: Context, fileName: String): List<TranslationEntity> {
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

fun replaceTextInHtml(html: String, translations: List<TranslationEntity>): String {
    // Сортировка translations по убыванию длины originalText
    val sortedTranslations = translations.sortedByDescending { it.originalText.length }

    // Поочередная замена текста, начиная с самых длинных фраз
    var modifiedHtml = html
    sortedTranslations.forEach { translation ->
        modifiedHtml = modifiedHtml.replace(translation.originalText, translation.translatedText)
    }
    return modifiedHtml
}

fun saveHtmlToFile(context: Context, htmlContent: String, fileName: String) {
    context.openFileOutput("$fileName.html", Context.MODE_PRIVATE).use { outputStream ->
        outputStream.write(htmlContent.toByteArray())
    }
}

private suspend fun loadHtmlFromAssets(context: Context, fileName: String): String {
    return withContext(Dispatchers.IO) {
        context.assets.open("$fileName.html").use { inputStream ->
            inputStream.bufferedReader().use(BufferedReader::readText)
        }


    }
}

fun processHtmlFile(
    context: Context,
    htmlFileName: String,
) {
    GlobalScope.launch {
        // Загрузка HTML из файловой системы
        val htmlContent = loadHtmlFromAssets(context, htmlFileName)
        // Чтение переводов из CSV-файла
        val translations = readCsvFile(context, htmlFileName)
        // Модификация HTML с использованием переводов
        val modifiedHtml = replaceTextInHtml(htmlContent, translations)
        // Сохранение модифицированного HTML обратно в файл
        saveHtmlToFile(context, modifiedHtml, htmlFileName)
    }
}




















