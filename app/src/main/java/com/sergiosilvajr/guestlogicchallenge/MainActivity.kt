package com.sergiosilvajr.guestlogicchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.sergiosilvajr.guestlogicchallenge.cardinfo.CardInfoContract
import com.sergiosilvajr.guestlogicchallenge.cardinfo.CardInfoFragment
import com.sergiosilvajr.guestlogicchallenge.flightroutes.FlightRoutesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CardInfoContract {
    val flightRoutesFragment = FlightRoutesFragment.newInstance()
    val cardFragment = CardInfoFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enable_card_button.setOnClickListener {
            if (cardFragment.isAdded) {
                removeCardFragment()
                enable_card_button.visibility = View.VISIBLE
            } else {
                initCardFragment()
                enable_card_button.visibility = View.GONE
            }
        }

        initFlightFragment()
    }

    private fun initFlightFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.map_frag_container, flightRoutesFragment)
            .commit()
    }

    private fun initCardFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.top_card_container, cardFragment)
            .commit()
    }

    private fun removeCardFragment() {
        supportFragmentManager
            .beginTransaction()
            .remove(cardFragment)
            .commit()
    }

    override fun onStartPressed(originCode: String, destinyCode: String) {
        flightRoutesFragment.searchRouteBetweenFlights(originCode, destinyCode)
    }

    override fun onClosedPressed() {
        enable_card_button.visibility = View.VISIBLE
        removeCardFragment()
    }
}
