package com.example.oshop.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oshop.data.CategoryDAO
import com.example.oshop.databinding.ItemCategoryBinding

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ItemCategoryBinding
    private lateinit var categoryDAO: CategoryDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryDAO = CategoryDAO(this)

        binding.btnSaveCategory.setOnClickListener {
            val categoryName = binding.etCategoryName.text.toString().trim()

            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Por favor, escribe un nombre", Toast.LENGTH_SHORT).show()
            } else {
                val result = categoryDAO.insertCategory(categoryName)
                if (result != -1L) {
                    Toast.makeText(this, "Categoría guardada", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
