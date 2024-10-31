package com.adquimo.core.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app_config_database.db"
        private const val DATABASE_VERSION = 1

        // Table name
        const val TABLE_CONFIG = "config"

        // SQL statement for creating the config table
        private const val CREATE_CONFIG_TABLE = """
            CREATE TABLE $TABLE_CONFIG (
                id INTEGER PRIMARY KEY,
                deviceId TEXT
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Execute the SQL statement to create the table
        db.execSQL(CREATE_CONFIG_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade as needed
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONFIG")
        onCreate(db)
    }
}