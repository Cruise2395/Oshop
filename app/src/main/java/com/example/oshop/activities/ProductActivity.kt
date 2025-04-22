package com.example.oshop.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oshop.R
import com.example.oshop.adapters.ProductAdapter
import com.example.oshop.data.Category
import com.example.oshop.data.CategoryDAO
import com.example.oshop.data.Product
import com.example.oshop.data.ProductDAO
import com.example.oshop.databinding.ActivityMainBinding
import com.example.oshop.databinding.ActivityProductBinding


        
        
class ProductActivity : AppCompatActivity() {

    companion object{
        const val CATEGORY_ID = "CATEGORY_ID"
        const val TASK_ID = "TASK_ID"
    }
    lateinit var binding: ActivityProductBinding
    lateinit var productDAO: ProductDAO
    lateinit var product: Product
    lateinit var categoryDAO: CategoryDAO
    lateinit var category: Category
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        val id = intent.getLongExtra(TASK_ID, -1L)
        val categoryId = intent.getLongExtra(CATEGORY_ID, -1L)
        
        productDAO = ProductDAO(this)
        categoryDAO = CategoryDAO(this)
        category = categoryDAO.findById(categoryId)!!

        if (id != -1L) {
            product = productDAO.findById(id)!!
            binding.titleEditText.setText(product.title)
            supportActionBar?.title = "Editar tarea"
        }else {
            product = Product(-1L, "", false, category)
            supportActionBar?.title = " Crear Tarea"
        }
        binding.saveButton.setOnClickListener { 
            val title = binding.titleEditText.text.toString()
            product.title = title

            if (product.id != -1L) {
                productDAO.update(product)
            }else{
                productDAO.insert(product)
            }
            finish()
        }
    }
}
