package com.example.happyplaceshrj.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaceshrj.R
import com.example.happyplaceshrj.model.HappyPlaceModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mHappyPlaceDetail: HappyPlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mHappyPlaceDetail = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS)
        mHappyPlaceDetail.let {
            if (it !=null) {
                setSupportActionBar(tool_bar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.title = it?.title
                tool_bar.setNavigationOnClickListener {
                    onBackPressed()
                }
                val supportMapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        /**
         * Add a marker on the location using the latitude and longitude and move
        the camera to it. */
        val position = LatLng(mHappyPlaceDetail!!.latitude, mHappyPlaceDetail!!.longitude)
        map!!.addMarker(MarkerOptions().position(position).title(mHappyPlaceDetail!!.location))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 20f)
        map?.animateCamera(newLatLngZoom)
    }
}

