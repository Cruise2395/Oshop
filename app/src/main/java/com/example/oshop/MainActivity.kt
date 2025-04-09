package com.example.oshop

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, "ProductosDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE categorias (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
            )
        """)
        db.execSQL("""
            CREATE TABLE productos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                precio REAL,
                categoria_id INTEGER,
                FOREIGN KEY(categoria_id) REFERENCES categorias(id)
            )
        """)

        // Datos iniciales
        db.execSQL("INSERT INTO categorias (nombre) VALUES ('Electrónica'), ('Ropa')")
        db.execSQL("INSERT INTO productos (nombre, precio, categoria_id) VALUES ('Laptop', 1000, 1), ('Camiseta', 20, 2)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS productos")
        db.execSQL("DROP TABLE IF EXISTS categorias")
        onCreate(db)
    }

    fun getCategorias(): List<Pair<Int, String>> {
        val list = mutableListOf<Pair<Int, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM categorias", null)
        while (cursor.moveToNext()) {
            list.add(Pair(cursor.getInt(0), cursor.getString(1)))
        }
        cursor.close()
        return list
    }

    fun getProductosPorCategoria(catId: Int): List<Triple<Int, String, Double>> {
        val list = mutableListOf<Triple<Int, String, Double>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, nombre, precio FROM productos WHERE categoria_id = ?", arrayOf(catId.toString()))
        while (cursor.moveToNext()) {
            list.add(Triple(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2)))
        }
        cursor.close()
        return list
    }

    fun borrarProducto(id: Int) {
        writableDatabase.delete("productos", "id = ?", arrayOf(id.toString()))
    }

    fun getProducto(id: Int): Triple<String, Double, Int>? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nombre, precio, categoria_id FROM productos WHERE id = ?", arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            val res = Triple(cursor.getString(0), cursor.getDouble(1), cursor.getInt(2))
            cursor.close()
            res
        } else {
            cursor.close()
            null
        }
    }

    fun actualizarProducto(id: Int, nombre: String, precio: Double) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("precio", precio)
        }
        db.update("productos", values, "id = ?", arrayOf(id.toString()))
    }
}
