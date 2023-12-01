package com.example.frhtml.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.frhtml.domain.ExtractRussianTextUseCase
import com.example.frhtml.domain.GetListOfBrandsUseCase
import com.example.frhtml.domain.LoadHtmlFromAssetsUseCase
import com.example.frhtml.domain.OutputExelUseCase
import com.example.frhtml.domain.OutputHtmlUseCase
import com.example.frhtml.domain.ReadCsvUseCase
import javax.inject.Inject


class MainViewModel  @Inject constructor(
    private val loadHtmlFromAssetsUseCase: LoadHtmlFromAssetsUseCase,
    private val extractRussianTextUseCase: ExtractRussianTextUseCase,
    private val outputExelUseCase: OutputExelUseCase,
    private val readCsvUseCase: ReadCsvUseCase,
    private val outputHtmlUseCase: OutputHtmlUseCase,
    private val context: Context,
    private val getListOfBrandsUseCase: GetListOfBrandsUseCase
) : ViewModel() {
    fun outputExel() {
        getListOfBrandsUseCase.invoke().forEach {
            val htmlString = loadHtmlFromAssetsUseCase(context, it.name)
            val textList = extractRussianTextUseCase(htmlString)
            outputExelUseCase(textList, it.name)
        }
    }
    fun readCsv(){
        getListOfBrandsUseCase.invoke().forEach {
            readCsvUseCase.invoke(context,it.name)
        }
        fun outputHtml(){
            getListOfBrandsUseCase.invoke().forEach {
                outputHtmlUseCase.invoke(context,it.name)
            }
        }


    }

}