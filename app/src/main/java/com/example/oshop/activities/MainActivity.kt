package com.example.oshop.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oshop.R
import com.example.oshop.adapters.CategoryAdapter
import com.example.oshop.data.Category
import com.example.oshop.data.CategoryDAO
import com.example.oshop.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var categoryDAO: CategoryDAO
    lateinit var categoryList: List<Category>
    lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryDAO = CategoryDAO(this)

        supportActionBar?.title = "Categorias"

        adapter = CategoryAdapter(emptyList(), ::showCategory, ::editCategory, ::deleteCategory)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addCategoryButton.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    fun refreshData(){
        categoryList = categoryDAO.findAll()
        adapter.updateItems(categoryList)
    }

    fun showCategory(position : Int){
        val category = categoryList[position]

        val intent = Intent(this, ProductsListActivity::class.java)
        intent.putExtra(ProductsListActivity.CATEGORY_ID, category.id)
        startActivity(intent)
    }
    fun editCategory(position: Int){
        val category = categoryList[position]

        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra(CategoryActivity.CATEGORY_ID, category.id)
        startActivity(intent)
    }

    fun deleteCategory(position: Int){
        val category = categoryList[position]

        AlertDialog.Builder(this)
            .setTitle("Delete category")
            .setMessage("Are you sure you want to delete this category?")
            .setPositiveButton(android.R.string.ok){ _,_ ->
                categoryDAO.delete(category)
                refreshData()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setCancelable(false)
            .show()
    }
}




