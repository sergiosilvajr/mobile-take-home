package com.sergiosilvajr.guestlogicchallenge.cardinfo

import android.content.res.AssetManager
import com.sergiosilvajr.guestlogicchallenge.data.FileManager
import com.sergiosilvajr.guestlogicchallenge.extensions.removeCommaFromSuffix

class CardInfoPresenter(val view: CardInfoContracts.View, private val fileManager: FileManager) : CardInfoContracts.Presenter {


    override fun checkIfCodeIsOk(originCode: String, destinyCode: String) {
        fileManager.initConfig()

        val originWithoutComma = originCode.removeCommaFromSuffix()
        val destinyWithoutComma = destinyCode.removeCommaFromSuffix()

        val originAirport = fileManager.airports.find { it.IATA == originWithoutComma }
        val destinyAirport = fileManager.airports.find { it.IATA == destinyWithoutComma }

        if (originWithoutComma.isEmpty() || originWithoutComma.isBlank()
            || destinyWithoutComma.isBlank() || destinyWithoutComma.isEmpty()
            || originAirport == null || destinyAirport == null) {
            view.showInvalidIATACodeError()
        } else {
            view.handleCodes(originWithoutComma, destinyWithoutComma)
        }
    }

    override fun getListOfValidAirportCodes(): List<String> {
        fileManager.initConfig()
        return fileManager.airports.filter { it.IATA != "\\N" }.map { it.IATA }
    }

}