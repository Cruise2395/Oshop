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
import com.example.oshop.databinding.ActivityProductBinding
import com.example.oshop.databinding.ActivityProductsListBinding

class ProductsListActivity: AppCompatActivity() {

    companion object{
        const val CATEGORY_ID = "CATEGORY_ID"
    }
    lateinit var binding: ActivityProductsListBinding
    lateinit var productDAO: ProductDAO
    lateinit var categoryDAO: CategoryDAO
    lateinit var productList: List<Product>
    lateinit var category: Category
    lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProductsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getLongExtra(CATEGORY_ID, -1L)

        productDAO = ProductDAO(this)
        categoryDAO = CategoryDAO(this)
        category= categoryDAO.findById(id)!!
        supportActionBar?.title = category.title

        adapter = ProductAdapter(emptyList(), ::editProduct, ::deleteProduct, ::checkProduct)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addProductButton.setOnClickListener{
            val intent = Intent(this, ProductActivity::class.java)
            intent.putExtra(ProductActivity.CATEGORY_ID, category.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }
    
    fun refreshData(){
        productList = productDAO.findAllByCategory(category)
        adapter.updateItems(productList)
    }

    fun checkProduct(position: Int) {
        val product = productList[position]

        product.done = !product.done
        productDAO.update(product)
        refreshData()
    }

    fun editProduct(position: Int) {
        val product = productList[position]

        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra(ProductActivity.TASK_ID, product.id)
        intent.putExtra(ProductActivity.CATEGORY_ID, category.id)
        startActivity(intent)
    }

    fun deleteProduct(position: Int) {
        val product = productList[position]

        AlertDialog.Builder(this)
            .setTitle("Delete product")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                productDAO.delete(product)
                refreshData()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setCancelable(false)
            .show()
    }
}