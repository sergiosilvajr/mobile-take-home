package com.sergiosilvajr.guestlogicchallenge.data.graph

import com.sergiosilvajr.guestlogicchallenge.data.model.Airport
import com.sergiosilvajr.guestlogicchallenge.data.model.Route

data class Graph(
    private val routes: List<Route>,
    private val airports: List<Airport>,
    private val origin: Airport,
    private val destiny: Airport
) {
    var edges: List<Edge> = routes.map { Edge(it.origin, it.destination) }
    private var vertices: List<Vertice>
    private var originVertice: Vertice?
    private var destinyVertice: Vertice?
    private var distanceCounter = 0

    init {
        vertices = airports.map {
            val vertice = Vertice(it.IATA)
            vertice.distance = MAX_DISTANCE
            vertice
        }
        originVertice = vertices.find { it.name == origin.IATA }
        destinyVertice = vertices.find { it.name == destiny.IATA }
    }

    fun getRouteVertices(): List<Vertice?> {
        originVertice!!.distance = 0
        originVertice!!.isVisited = true
        originVertice!!.before = null

        do {
            val vertices = getVerticesFromOriginWithDistance(distanceCounter).filterNotNull()
            vertices.forEach { origin ->
                val neighbors = getNeighboors(origin).filterNotNull()
                neighbors
                    .filter { !it.isVisited }
                    .forEach { neighboor ->
                        neighboor.before = origin.name
                        neighboor.distance = distanceCounter + 1
                        neighboor.isVisited = true
                    }
            }

            distanceCounter++
        } while ((vertices.isNotEmpty() || !destinyVertice!!.isVisited) && distanceCounter < NO_EXISTING_ROUTE_MAX_DISTANCE)

        return getVerticeSequenceToDestiny()
    }

    private fun getVerticesFromOriginWithDistance(distance: Int): List<Vertice?> {
        return vertices.filter {
            it.distance == distance
        }
    }

    private fun getVerticeSequenceToDestiny(): List<Vertice> {
        var currentVertice = destinyVertice
        var verticePathList = arrayListOf<Vertice>()
        verticePathList.add(currentVertice!!)
        while (currentVertice!!.before != null) {
            val foundVertice = vertices.first { it.name == currentVertice!!.before }
            verticePathList.add(foundVertice)
            currentVertice = foundVertice
        }

        val pathList =  verticePathList.reversed()

        if (pathList.size <= 1) {
            return listOf()
        } else {
            return pathList
        }
    }

    private fun getNeighboors(vertice: Vertice): List<Vertice?> {
        val edgesWithVerticeAsOrigin = edges.filter { edge -> edge.origin == vertice.name }
        val bounderedVertices =
            edgesWithVerticeAsOrigin.map { edge ->
                vertices
                    .find { vertice -> vertice.name == edge.destiny }
            }
        return bounderedVertices
    }

    companion object {
        const val MAX_DISTANCE = Int.MAX_VALUE / 2
        const val NO_EXISTING_ROUTE_MAX_DISTANCE = 100
    }
}