package com.example.oshop.data

class ProductDAO(private val context: Context) {
    private val dbHelper = DataBaseManager(context)

    fun insertCategory(name: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", name)
        }
        return db.insert("Category", null, values)
    }

    fun getAllCategories(): List<Category> {
        val db = dbHelper.readableDatabase
        val list = mutableListOf<Category>()
        val cursor = db.rawQuery("SELECT * FROM Category", null)

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val name = getString(getColumnIndexOrThrow("name"))
                list.add(Category(id, name))
            }
            close()
        }
        return list
    }
}
