package com.sergiosilvajr.guestlogicchallenge.cardinfo

interface CardInfoContract {
    fun onStartPressed(originCode: String, destinyCode: String)

    fun onClosedPressed()
}