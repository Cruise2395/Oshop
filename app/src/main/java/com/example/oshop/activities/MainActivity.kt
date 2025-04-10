package com.example.oshop.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oshop.adapters.CategoryAdapter
import com.example.oshop.data.CategoryDAO
import com.example.oshop.databinding.ActivityMainBinding
import com.example.oshop.models.Category

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryDAO: CategoryDAO
    lateinit var categoryList: List<Category>
    lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryDAO = CategoryDAO(this)

        setupRecyclerView()
        loadCategories()

        binding.btnAddCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivityForResult(intent, ADD_CATEGORY_REQUEST)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerCategories.layoutManager = LinearLayoutManager(this)
        categoryAdapter = CategoryAdapter(emptyList()) { category ->
            // Abre la lista de productos de esa categoría
            val intent = Intent(this, ProductListsActivity::class.java)
            intent.putExtra("categoryId", category.id)
            startActivity(intent)
        }
        binding.recyclerCategories.adapter = categoryAdapter
    }

    private fun loadCategories() {
        val categories = categoryDAO.getAllCategories()
        categoryAdapter.updateData(categories)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CATEGORY_REQUEST && resultCode == Activity.RESULT_OK) {
            loadCategories()
        }
    }

    companion object {
        private const val ADD_CATEGORY_REQUEST = 1001
    }
}
