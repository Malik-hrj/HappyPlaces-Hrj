package com.example.happyplaceshrj.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaceshrj.R
import com.example.happyplaceshrj.adapter.HappyPlaceAdapter
import com.example.happyplaceshrj.callback.SwipeToDeleteCallback
import com.example.happyplaceshrj.callback.SwipeToEditCallback
import com.example.happyplaceshrj.database.DatabaseHandler
import com.example.happyplaceshrj.model.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        val EXTRA_PLACE_DETAILS = "extra_place_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_add_happy_place.setOnClickListener {
            val intent = Intent( this@MainActivity, AddHappyPlaceActivity:: class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }

      //  getHappyPlacesFromLocalDB()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
            //    getHappyPlacesFromLocalDB()
            } else {
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    private fun getHappyPlacesFromLocalDB() {
        val dbHandler = DatabaseHandler(this)

        val getHappyPlaceList = dbHandler.getHappyPlaceList()

        if (getHappyPlaceList.size > 0) {
            rv_happy_places_list.visibility = View.GONE
            tv_no_records.visibility = View.VISIBLE

            setupHappyPlaceRecyclerView(getHappyPlaceList)
        } else {
            rv_happy_places_list.visibility = View.GONE
            tv_no_records.visibility = View.VISIBLE
        }
    }

    private fun setupHappyPlaceRecyclerView(happyPlaceList: ArrayList<HappyPlaceModel>) {
        rv_happy_places_list.layoutManager = LinearLayoutManager(this)
        rv_happy_places_list.setHasFixedSize(true)

        val placesAdapter = HappyPlaceAdapter(this, happyPlaceList)
        rv_happy_places_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object : HappyPlaceAdapter.OnClickListener {
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, PlaceDetailActivity:: class.java )
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_happy_places_list.adapter as HappyPlaceAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_happy_places_list)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_happy_places_list.adapter as HappyPlaceAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlacesFromLocalDB()
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_happy_places_list)


    }
}


