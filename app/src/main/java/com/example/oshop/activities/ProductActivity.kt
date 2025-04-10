package com.example.oshop.activities

class ProductActivity : AppCompatActivity() {
    lateinit var db: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = SQLiteHelper(this)
        val productoId = intent.getIntExtra("productoId", -1)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        val nombreInput = EditText(this)
        val precioInput = EditText(this).apply { inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL }
        val btnGuardar = Button(this).apply { text = "Guardar" }

        layout.addView(nombreInput)
        layout.addView(precioInput)
        layout.addView(btnGuardar)

        setContentView(layout)

        val producto = db.getProducto(productoId)
        producto?.let {
            nombreInput.setText(it.first)
            precioInput.setText(it.second.toString())
        }

        btnGuardar.setOnClickListener {
            val nombre = nombreInput.text.toString()
            val precio = precioInput.text.toString().toDoubleOrNull() ?: 0.0
            db.actualizarProducto(productoId, nombre, precio)
            finish()
        }
    }
}
