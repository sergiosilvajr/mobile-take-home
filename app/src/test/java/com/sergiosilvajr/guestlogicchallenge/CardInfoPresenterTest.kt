package com.sergiosilvajr.guestlogicchallenge

import com.sergiosilvajr.guestlogicchallenge.cardinfo.CardInfoContracts
import com.sergiosilvajr.guestlogicchallenge.cardinfo.CardInfoPresenter
import com.sergiosilvajr.guestlogicchallenge.data.FileManager
import com.sergiosilvajr.guestlogicchallenge.data.model.Airport
import com.sergiosilvajr.guestlogicchallenge.data.model.Route
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CardInfoPresenterTest {

    @Mock
    lateinit var view: CardInfoContracts.View

    @Mock
    lateinit var fileManager: FileManager

    lateinit var presenter: CardInfoContracts.Presenter
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        presenter = CardInfoPresenter(view, fileManager)
    }

    @Test
    fun checkIfCodeIsOkWhenCodeIsNotEmpty() {
        var originCode = "AAA"
        val destinyCode = "BBB"

        Mockito.doNothing().`when`(fileManager).initConfig()

        fileManager.airports = listOf(
            Airport("name", "city", "country", originCode, 0.0, 0.0),
            Airport("name", "city", "country", destinyCode, 1.0, 1.0)
        )

        fileManager.routes = listOf(
            Route("airline", originCode, destinyCode)
        )

        presenter.checkIfCodeIsOk(originCode, destinyCode)

        Mockito.verify(view).handleCodes(originCode, destinyCode)
    }


    @Test
    fun checkIfCodeIsOkWhenCodeIsEmpty() {
        var originCode = "AAA"
        val destinyCode = "BBB"

        Mockito.doNothing().`when`(fileManager).initConfig()

        fileManager.airports = listOf(
            Airport("name", "city", "country", originCode, 0.0, 0.0),
            Airport("name", "city", "country", destinyCode, 1.0, 1.0)
        )

        fileManager.routes = listOf(
            Route("airline", originCode, destinyCode)
        )

        presenter.checkIfCodeIsOk("", "")

        Mockito.verify(view).showInvalidIATACodeError()
    }

    @Test
    fun getListOfValidAirportsCodeWithValidAirports() {
        var originCode = "AAA"
        val destinyCode = "BBB"

        Mockito.doNothing().`when`(fileManager).initConfig()

        fileManager.airports = listOf(
            Airport("name", "city", "country", originCode, 0.0, 0.0),
            Airport("name", "city", "country", destinyCode, 1.0, 1.0)
        )

        fileManager.routes = listOf(
            Route("airline", originCode, destinyCode)
        )

        val airportCodeList = presenter.getListOfValidAirportCodes()

        Assert.assertEquals(airportCodeList, fileManager.airports.map{it.IATA})
    }

    @Test
    fun getListOfValidAirportsCodeWithInValidAirports() {
        var originCode = "AAA"
        val destinyCode = "BBB"

        Mockito.doNothing().`when`(fileManager).initConfig()

        fileManager.airports = listOf(
            Airport("name", "city", "country", originCode, 0.0, 0.0),
            Airport("name", "city", "country", "\\N", 1.0, 1.0)
        )

        fileManager.routes = listOf(
            Route("airline", originCode, destinyCode)
        )

        val airportCodeList = presenter.getListOfValidAirportCodes()

        Assert.assertNotEquals(airportCodeList, fileManager.airports.map{it.IATA})
    }
}