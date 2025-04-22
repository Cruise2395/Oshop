package com.example.oshop.data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.oshop.utils.DataBaseManager


class CategoryDAO(context: Context) {

    val dataBaseManager = DataBaseManager(context)

    fun insert(category: Category) {
        //gets teh data repository in write mode
        val db = dataBaseManager.writableDatabase

        //Creates a new map of values, where column names are the key
        val values = ContentValues().apply {
            put(Category.COLUMN_NAME_TITLE, category.title)
        }

        try {
            //insert the new row, returning the primary key value of the new row
            val newRowId = db.insert(Category.TABLE_NAME, null, values)

            Log.i("DATABASE", "Inserted category with id: ${newRowId}")
        } catch (e: Exception){
            e.printStackTrace()
        }finally {
            db.close()
        }
    }

    fun update(category: Category) {
        //gets the data repository en write mode
        val db = dataBaseManager.writableDatabase

        //Creates a new map ofg values, where column names are the keys
        val values = ContentValues().apply {
            put(Category.COLUMN_NAME_TITLE, category.title)
        }
        try{
            val updatedRows = db.update(Category.TABLE_NAME, values, "${Category.COLUMN_NAME_ID} = ${category.id}", null)

            Log.i("DATABASE", "Updated category with id: ${category.id}")
        }catch (e: Exception){
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun delete(category: Category) {
        val db = dataBaseManager.writableDatabase

        try {
            val deletedRows = db.delete(Category.TABLE_NAME, "${Category.COLUMN_NAME_ID} = ${category.id}", null)
            Log.i("DATABASE", "Deleted category with id: ${category.id}")
        } catch (e: Exception){
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun findById(id: Long): Category? {
        val db = dataBaseManager.readableDatabase

        val projection = arrayOf(
            Category.COLUMN_NAME_ID,
            Category.COLUMN_NAME_TITLE
        )

        val selection = "${Category.COLUMN_NAME_ID} = $id"
        var category: Category? = null

        try {
            val cursor = db.query(
                Category.TABLE_NAME,  // the table to query
                projection, // the array of columns to return (pass null to get all)
                selection, //the columns for the where clause
                null, //the values for the where clause
                null, //dont group the rows
                null, // dont filter by row groups
                null // the sort order
            )

            if(cursor.moveToNext()){
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE))

                category = Category(id, title)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            db.close()
        }
        return category
    }

    fun findAll(): List<Category> {
        val db = dataBaseManager.readableDatabase

        val projection = arrayOf(
            Category.COLUMN_NAME_ID,
            Category.COLUMN_NAME_TITLE
        )
        var categoryList: MutableList<Category> = mutableListOf()

        try {
            val cursor = db.query(
                Category.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE))

                val category = Category(id, title)
                categoryList.add(category)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            db.close()
        }
        return categoryList
    }

}
