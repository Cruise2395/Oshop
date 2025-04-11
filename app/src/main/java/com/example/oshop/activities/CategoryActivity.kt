package com.example.oshop.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.oshop.data.Category
import com.example.oshop.data.CategoryDAO
import com.example.oshop.databinding.ActivityCategoryBinding
import com.example.oshop.

class CategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryBinding
    lateinit var categoryDAO: CategoryDAO
    lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryDAO = CategoryDAO(this)

        val id = intent.getLongExtra(CATEGORY_ID, -1L)

        if (id != -1L){
            category = categoryDAO.findById(id)!!
            binding.titleEditText.setText(category.title)
            supportActionBar?.title = "Editar categoria"
        } else {
            category = Category(-1L, "")
            supportActionBar?.title = "Crear tarea"
        }

        binding.saveButton.setOnClickListener{
            val title = binding.titleEditText.text.toString()

            category.title = title

            if (category.id != -1L){
                categoryDAO.update(category)
            } else {
                categoryDAO.insert(category)
            }
            finish()
        }
    }
}

