package com.sergiosilvajr.guestlogicchallenge.flightroutes

import com.sergiosilvajr.guestlogicchallenge.data.FileManager
import com.sergiosilvajr.guestlogicchallenge.data.graph.Graph

class FlightRoutePresenter(
    private val view: FlightRouteContracts.View,
    private val fileManager: FileManager
) : FlightRouteContracts.Presenter {

    override fun searchRoutesBetweenAirports(originAirportCode: String, destinyAirportCode: String) {
        val routes = fileManager.routes
        val airports = fileManager.airports

        val originAirport = airports.find { it.IATA == originAirportCode }
        val destinyAirport = airports.find { it.IATA == destinyAirportCode }

        if (originAirport != null && destinyAirport != null) {
            val graph = Graph(routes, airports, originAirport, destinyAirport)
            val sequenceList = graph.getRouteVertices()

            val airports = sequenceList.map { vertice ->
                airports.find { vertice?.name == it.IATA }
            }.filterNotNull().map {
                Triple(it.IATA, it.latitude, it.longitude)
            }
            if (airports.size <= 1) {
                view.showNotExistingRouteBetweenAirportsError()
            } else {
                view.showAirportsMarksOnMap(airports)
            }
        } else {
            view.showMissingAirportError()
        }
    }
}