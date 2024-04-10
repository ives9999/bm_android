package com.sportpassword.bm.Controllers

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sportpassword.bm.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MyMapVC : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var title: String = ""
    var address: String = ""
    var lat: Double = 25.033920
    var lng: Double = 121.441990

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_map_vc)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")!!
        }

        if (intent.hasExtra("address")) {
            address = intent.getStringExtra("address")!!
        }

        setMyTitle(title)


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        mMap.isMyLocationEnabled = true

        try {
//            println(address)
            val geocoder = Geocoder(this)
            val latlngs = geocoder.getFromLocationName(address, 1)
            if (latlngs != null && latlngs.size > 0) {
                lat = latlngs[0].latitude
                lng = latlngs[0].longitude
//                println(lat)
//                println(lng)
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
        }

        // Add a marker in Sydney and move the camera
        val location = LatLng(lat, lng)
        val marker = mMap.addMarker(MarkerOptions().position(location).title(title))
        marker?.showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 18f))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}















