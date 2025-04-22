package com.example.oshop.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.oshop.data.Category
import com.example.oshop.data.Product

class DataBaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val DATABASE_NAME="oshop.db"
        const val DATABASE_VERSION= 1

        private const val SQL_CREATE_TABLE_PRODUCT =
            "CREATE TABLE ${Product.TABLE_NAME} (" +
                    "${Product.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${Product.COLUMN_NAME_TITLE} TEXT," +
                    "${Product.COLUMN_NAME_DONE} BOOLEAN," +
                    "${Product.COLUMN_NAME_CATEGORY} INTEGER," +
                    "FOREIGN KEY(${Product.COLUMN_NAME_CATEGORY})" +
                    "REFERENCES ${Category.TABLE_NAME}(${Category.COLUMN_NAME_ID}) ON DELETE CASCADE)"

        private const val SQL_DROP_TABLE_PRODUCT = "DROP TABLE IF EXISTS ${Product.TABLE_NAME}"

        private const val SQL_CREATE_TABLE_CATEGORY =
            "CREATE TABLE ${Category.TABLE_NAME} ("+
                    "${Category.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${Category.COLUMN_NAME_TITLE} TEXT)"

        private const val SQL_DROP_TABLE_CATEGORY = "DROP TABLE IF EXISTS ${Category.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_CATEGORY)
        db.execSQL(SQL_CREATE_TABLE_PRODUCT)
        Log.i("DATABASE", "Created table Products")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DROP_TABLE_PRODUCT)
        db.execSQL(SQL_DROP_TABLE_CATEGORY)
        onCreate(db)
    }

    private fun onDestroy(db:SQLiteDatabase){
        db.execSQL(SQL_DROP_TABLE_PRODUCT)
        db.execSQL(SQL_DROP_TABLE_CATEGORY)
    }
}
