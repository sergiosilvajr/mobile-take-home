package com.sergiosilvajr.guestlogicchallenge.flightroutes

interface FlightRouteContracts {
    interface View {
        fun showNotExistingRouteBetweenAirportsError()

        fun showAirportsMarksOnMap(nameAndCoordinateList: List<Triple<String, Double, Double>>)

        fun showMissingAirportError()
    }
    interface Presenter {
        fun searchRoutesBetweenAirports(originAirportCode: String, destinyAirportCode: String)
    }
}

