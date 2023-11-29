package com.example.frhtml.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.frhtml.domain.*


class MainViewModel(
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