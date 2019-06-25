package com.sergiosilvajr.guestlogicchallenge.cardinfo

interface CardInfoContracts {
    interface View {
        fun showInvalidIATACodeError()

        fun handleCodes(originCode: String, destinyCode: String)
    }

    interface Presenter {
        fun checkIfCodeIsOk(originCode: String, destinyCode: String)

        fun getListOfValidAirportCodes(): List<String>
    }
}