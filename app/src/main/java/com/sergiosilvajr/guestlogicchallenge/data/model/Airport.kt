package com.sergiosilvajr.guestlogicchallenge.data.model

//Name,City,Country,IATA 3,Latitute ,Longitude

data class Airport(val name: String,
                   val city: String,
                   val country: String,
                   val IATA: String,
                   val latitude: Double,
                   val longitude: Double)