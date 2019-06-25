package com.sergiosilvajr.guestlogicchallenge.extensions

fun String.removeCommaFromSuffix(): String {
    return this.replace(",","").trim()
}