package com.sergiosilvajr.guestlogicchallenge.data

import android.content.res.AssetManager
import com.sergiosilvajr.guestlogicchallenge.data.model.Airline
import com.sergiosilvajr.guestlogicchallenge.data.model.Airport
import com.sergiosilvajr.guestlogicchallenge.data.model.Route
import java.io.File

open class FileManager(val assetManager: AssetManager) {
    lateinit var airlines: List<Airline>
    lateinit var airports: List<Airport>
    lateinit var routes: List<Route>

    open fun initConfig() {
        val airlinesFileContent =  assetManager.open("airlines.csv").bufferedReader().use {it.readText() }
        val airportsFileContent =  assetManager.open("airports.csv").bufferedReader().use {it.readText() }
        val routesFileContent =  assetManager.open("routes.csv").bufferedReader().use {it.readText() }

        airlines = getContentFromAirlinesFile(airlinesFileContent)
        airports = getContentFromAirportsFile(airportsFileContent)
        routes = getContentFromRoutesFile(routesFileContent)
    }

    private fun getFileLines(fileContent: String): List<String> {
        val splitLines = fileContent.split("\r\n")
        return splitLines.subList(1, splitLines.count())
    }

    private fun getContentFromAirlinesFile(fileContent: String): List<Airline> {
        val lines = getFileLines(fileContent)
        return lines.map { line ->
            val splitLine = line.split(",")
            Airline(splitLine[0], splitLine[1], splitLine[2], splitLine[3])
        }
    }

    private fun getContentFromRoutesFile(fileContent: String): List<Route> {
        val lines = getFileLines(fileContent)
        return lines.map { line ->
            val splitLine = line.split(",")
            Route(splitLine[0], splitLine[1], splitLine[2])
        }
    }

    private fun getContentFromAirportsFile(fileContent: String): List<Airport> {
        val lines = getFileLines(fileContent)
        return lines.map { line ->
            val splitLine = line.split(",")
            if (line.split(", ").count() > 1) {
                Airport(
                    splitLine[0] + splitLine[1], splitLine[2], splitLine[3], splitLine[4],
                    splitLine[5].toDouble(), splitLine[6].toDouble()
                )
            } else {
                Airport(
                    splitLine[0], splitLine[1], splitLine[2], splitLine[3],
                    splitLine[4].toDouble(), splitLine[5].toDouble()
                )
            }
        }
    }
}