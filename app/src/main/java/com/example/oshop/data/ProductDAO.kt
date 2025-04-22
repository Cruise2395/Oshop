package com.example.oshop.data


import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.oshop.utils.DatabaseManager


class ProductDAO(context: Context) {

    val dataBaseManager = DataBaseManager(context)
    val categoryDAO = CategoryDAO(context)

    fun insert(product: Product) {
        val db = dataBaseManager.writableDatabase

        val values = ContentValues().apply {
            put(Product.COLUMN_NAME_TITLE, product.title)
            put(Product.COLUMN_NAME_DONE, product.done)
            put(Product.COLUMN_NAME_CATEGORY, product.category.id)
        }

        try {
            val newRowId = db.insert(Product.TABLE_NAME, null, values)

            Log.i("DATABASE",  "Inserted task with id: $newRowId")
        }catch (e: Exception){
            e.printStackTrace()
        } finally {
            db.close()
        }
    }


    fun update(product: Product) {
        val db = dataBaseManager.writableDatabase

        val values = ContentValues().apply{
            put(Product.COLUMN_NAME_TITLE, product.title)
            put(Product.COLUMN_NAME_DONE, product.done)
            put(Product.COLUMN_NAME_CATEGORY, product.category.id)
        }
        try {
            val updatedRows = db.update(Product.TABLE_NAME, values, "${Product.COLUMN_NAME_ID} = ${product.id}", null)

            Log.i("DATABASE", "Updated task with id: ${product.id}")
        }catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun delete(product: Product) {
        val db = dataBaseManager.writableDatabase

        try {
            val deletedRows = db.delete(Product.TABLE_NAME, "${Product.COLUMN_NAME_ID} = ${product.id}", null)

            Log.i("DATABASE", "Deleted task with id: ${product.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun findById(id: Long): Product?{
        val db = dataBaseManager.readableDatabase

        val projection = arrayOf(
            Product.COLUMN_NAME_ID,
            Product.COLUMN_NAME_TITLE,
            Product.COLUMN_NAME_DONE,
            Product.COLUMN_NAME_CATEGORY
        )

        val selection = "${Product.COLUMN_NAME_ID} = $id"

        var product: Product? = null

        try {
            val cursor = db.query(
                Product.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            if (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_TITLE))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_DONE)) != 0
                val categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_CATEGORY))
                val category = categoryDAO.findById(categoryId)!!
                product = Product(id, title, done, category)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

        return product
    }

    fun findAll(): List<Product> {
        val db = dataBaseManager.readableDatabase

        val projection = arrayOf(
            Product.COLUMN_NAME_ID,
            Product.COLUMN_NAME_TITLE,
            Product.COLUMN_NAME_DONE,
            Product.COLUMN_NAME_CATEGORY
        )

        var productList: MutableList<Product> = mutableListOf()

        try {
            val cursor = db.query(
                Product.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                Product.COLUMN_NAME_DONE               // The sort order
            )

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_TITLE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_DONE)) != 0
            val categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_CATEGORY))
            val category = categoryDAO.findById(categoryId)!!
            val product = Product(id, title, done, category)

            productList.add(product)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        db.close()
    }

    return productList
}

    fun findAllByCategory(category: Category): List<Product>{
        val db = dataBaseManager.readableDatabase

        val projection = arrayOf(
            Product.COLUMN_NAME_ID,
            Product.COLUMN_NAME_TITLE,
            Product.COLUMN_NAME_DONE,
            Product.COLUMN_NAME_CATEGORY
        )

        val selection = "${Product.COLUMN_NAME_CATEGORY} = ${category.id}"

        var productList: MutableList<Product> = mutableListOf()

        try {
            val cursor = db.query(
                Product.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                Product.COLUMN_NAME_DONE               // The sort order
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_TITLE))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_DONE)) != 0
                val categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_CATEGORY))
                val category = categoryDAO.findById(categoryId)!!
                val task = Product(id, title, done, category)

                productList.add(task)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

        return productList
    }
}
