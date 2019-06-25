package com.sergiosilvajr.guestlogicchallenge

import android.content.res.AssetManager
import com.sergiosilvajr.guestlogicchallenge.data.FileManager
import com.sergiosilvajr.guestlogicchallenge.data.model.Airport
import com.sergiosilvajr.guestlogicchallenge.data.model.Route
import com.sergiosilvajr.guestlogicchallenge.flightroutes.FlightRouteContracts
import com.sergiosilvajr.guestlogicchallenge.flightroutes.FlightRoutePresenter
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class FlightRouterPresenterTest {

    @Mock
    lateinit var view: FlightRouteContracts.View

    @Mock
    lateinit var fileManager: FileManager

    lateinit var presenter: FlightRouteContracts.Presenter
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = FlightRoutePresenter(view, fileManager)
    }

    @Test
    fun searchSimpleRouteBetweenOneTripAirports() {
        val originCode = "AAA"
        val destinyCode = "BBB"

        doNothing().`when`(fileManager).initConfig()

        fileManager.airports = listOf(
            Airport("name", "city", "country", originCode, 0.0, 0.0),
            Airport("name", "city", "country", destinyCode, 1.0, 1.0)
        )

        fileManager.routes = listOf(
            Route("airline", originCode, destinyCode)
        )

        presenter.searchRoutesBetweenAirports(originCode, destinyCode)

        verify(view).showAirportsMarksOnMap(ArgumentMatchers.anyList())
    }

    @Test
    fun searchSimpleRouteBetweenMoreThanOneTripAirports() {
        val originCode = "AAA"
        val destinyCode = "BBB"
        val intermediateAirport = "CCC"
        doNothing().`when`(fileManager).initConfig()

        fileManager.airports = listOf(
            Airport("name", "city", "country", originCode, 0.0, 0.0),
            Airport("name", "city", "country", destinyCode, 1.0, 1.0),
            Airport("name", "city", "country", intermediateAirport, 1.0, 1.0)
        )

        fileManager.routes = listOf(
            Route("airline", originCode, intermediateAirport),
            Route("airline", intermediateAirport, destinyCode)
        )

        presenter.searchRoutesBetweenAirports(originCode, destinyCode)

        verify(view).showAirportsMarksOnMap(ArgumentMatchers.anyList())
    }

    @Test
    fun searchWithNoRoute() {
        val originCode = "AAA"
        val destinyCode = "BBB"
        val intermediateAirport = "CCC"
        doNothing().`when`(fileManager).initConfig()

        fileManager.airports = listOf(
            Airport("name", "city", "country", originCode, 0.0, 0.0),
            Airport("name", "city", "country", destinyCode, 1.0, 1.0),
            Airport("name", "city", "country", intermediateAirport, 1.0, 1.0)
        )

        fileManager.routes = listOf(
            Route("airline", originCode, intermediateAirport)
        )

        presenter.searchRoutesBetweenAirports(originCode, destinyCode)

        verify(view).showNotExistingRouteBetweenAirportsError()
    }

    @Test
    fun searchAndShowMissingAirportError() {
        val originCode = "AAA"
        val destinyCode = "BBB"
        val intermediateAirport = "CCC"

        doNothing().`when`(fileManager).initConfig()

        fileManager.airports = listOf(
            Airport("name", "city", "country", "ZZZ", 0.0, 0.0),
            Airport("name", "city", "country", "YYY ", 1.0, 1.0)
        )

        fileManager.routes = listOf(
            Route("airline", originCode, intermediateAirport)
        )

        presenter.searchRoutesBetweenAirports(originCode, destinyCode)

        verify(view).showMissingAirportError()
    }
}
