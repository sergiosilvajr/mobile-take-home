package com.sergiosilvajr.guestlogicchallenge.flightroutes

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.sergiosilvajr.guestlogicchallenge.R
import com.sergiosilvajr.guestlogicchallenge.data.FileManager

class FlightRoutesFragment : Fragment(), OnMapReadyCallback, FlightRouteContracts.View {
    private val supportMapFragment = SupportMapFragment.newInstance()
    private var mapReference: GoogleMap? = null
    lateinit var presenter: FlightRouteContracts.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_airports, container, false)
        val fileManager = FileManager(activity!!.assets)
        fileManager.initConfig()
        presenter = FlightRoutePresenter(this, fileManager)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        supportMapFragment.getMapAsync(this)
        childFragmentManager
            .beginTransaction()
            .add(R.id.map_container, supportMapFragment)
            .commit()
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.uiSettings?.isZoomControlsEnabled = true
        map?.uiSettings?.isMapToolbarEnabled = true
        mapReference = map
        mapReference?.clear()
    }

    fun searchRouteBetweenFlights(originAirportCode: String, destinyAirportCode: String) {
        Thread(Runnable {
            presenter.searchRoutesBetweenAirports(originAirportCode, destinyAirportCode)
        }).start()
    }

    override fun showNotExistingRouteBetweenAirportsError() {
        activity?.runOnUiThread {
            val alertDialog = AlertDialog.Builder(activity!!).create()
            alertDialog.setTitle(getString(R.string.error_title))
            alertDialog.setMessage(getString(R.string.error_missing_route))
            alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            alertDialog.show()
        }
    }

    override fun showAirportsMarksOnMap(nameAndCoordinateList: List<Triple<String, Double, Double>>) {
        activity?.runOnUiThread {
            mapReference?.clear()
            nameAndCoordinateList.forEach {
                val latLng = LatLng(it.second, it.third)
                val markerOptions = MarkerOptions().position(latLng).title(it.first)
                mapReference?.addMarker(markerOptions)
            }

            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(DEFAULT_LINE_WIDTH)
                .addAll(nameAndCoordinateList.map { LatLng(it.second, it.third) })
            polylineOptions.visible(true)
            polylineOptions.geodesic(true)
            mapReference?.addPolyline(polylineOptions)
        }
    }

    override fun showMissingAirportError() {
        activity?.runOnUiThread {

        }
    }


    companion object {
        const val DEFAULT_LINE_WIDTH = 2F
        fun newInstance(): FlightRoutesFragment {
            return FlightRoutesFragment()
        }
    }
}