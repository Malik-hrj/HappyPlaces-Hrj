package com.example.happyplaceshrj.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.happyplaceshrj.activity.AddHappyPlaceActivity
import com.example.happyplaceshrj.model.HappyPlaceModel

class DatabaseHandler (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "HappyPlacesDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_HAPPY_PLACE = "HappyPlaceTables"
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTIONS = "descriptions"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATIONS = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_HAPPY_PLACE_TABLE = ("CREATE TABLE "
                + TABLE_HAPPY_PLACE + "("
                + KEY_ID + "INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTIONS + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATIONS + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        p0?.execSQL(CREATE_HAPPY_PLACE_TABLE)

    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {
        database!!.execSQL("DROP TABLE IF EXISTS $TABLE_HAPPY_PLACE")
        onCreate(database)

    }

    fun addHappyPlace(happyPlace: HappyPlaceModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()

        contentValues.put(KEY_TITLE, happyPlace.title)
        contentValues.put(KEY_IMAGE, happyPlace.image)
        contentValues.put(KEY_DESCRIPTIONS, happyPlace.description)
        contentValues.put(KEY_DATE, happyPlace.date)
        contentValues.put(KEY_LOCATIONS, happyPlace.location)
        contentValues.put(KEY_LATITUDE, happyPlace.latitude)
        contentValues.put(KEY_LONGITUDE, happyPlace.longitude)

        val result = db.insert(TABLE_HAPPY_PLACE, null, contentValues)

        db.close()
        return result
    }

    fun updateHappyPlace(happyPlace : HappyPlaceModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()

        contentValues.put(KEY_TITLE, happyPlace.title)
        contentValues.put(KEY_IMAGE, happyPlace.image)
        contentValues.put(KEY_DESCRIPTIONS, happyPlace.description)
        contentValues.put(KEY_DATE, happyPlace.date)
        contentValues.put(KEY_LOCATIONS, happyPlace.location)
        contentValues.put(KEY_LATITUDE, happyPlace.latitude)
        contentValues.put(KEY_LONGITUDE, happyPlace.longitude)

        val succes = db.update(TABLE_HAPPY_PLACE, contentValues, KEY_ID + "=" + happyPlace.id, null)

        db.close()
        return succes
    }

    fun deleteHappyPlace(happyPlace: HappyPlaceModel) : Int {
        val db = this.writableDatabase
        val succes = db.delete(TABLE_HAPPY_PLACE, KEY_ID +"="+ happyPlace.id, null)

        db.close()
        return succes
    }

    fun getHappyPlaceList(): ArrayList<HappyPlaceModel> {
        val happyPlaceList = ArrayList<HappyPlaceModel>()
        val selectQuery = "SELECT * FROM $TABLE_HAPPY_PLACE"
        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val place = HappyPlaceModel(
                            cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                            cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTIONS)),
                            cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                            cursor.getString(cursor.getColumnIndex(KEY_LOCATIONS)),
                            cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                            cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                    )
                    happyPlaceList.add(place)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return happyPlaceList
    }
}