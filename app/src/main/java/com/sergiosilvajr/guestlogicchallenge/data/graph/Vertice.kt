package com.sergiosilvajr.guestlogicchallenge.data.graph

data class Vertice(val name: String) {
    var isVisited: Boolean = false
    var distance = Graph.MAX_DISTANCE
    var before: String? = null
}