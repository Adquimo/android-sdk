package com.adquimo.core.database.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.adquimo.core.database.AppDatabase
import com.adquimo.core.database.entities.Config


class ConfigDao(context: Context) {

    private val dbHelper = AppDatabase(context)

    @SuppressLint("Range")
    fun getConfig(): Config? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT deviceId FROM ${AppDatabase.TABLE_CONFIG} WHERE id = 1", null)

        return if (cursor.moveToFirst()) {
            val deviceId = cursor.getString(cursor.getColumnIndex("deviceId"))
            cursor.close()
            Config(deviceId = deviceId)
        } else {
            cursor.close()
            null
        }
    }

    fun insertConfig(deviceId: String) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id", 1) // Always insert with id = 1
            put("deviceId", deviceId)
        }
        db.insertWithOnConflict(AppDatabase.TABLE_CONFIG, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun deleteConfig() {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        db.execSQL("DELETE FROM ${AppDatabase.TABLE_CONFIG}")
    }

    fun dbExists(): Boolean {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='${AppDatabase.TABLE_CONFIG}'", null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}